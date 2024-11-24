package vn.edu.iuh.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.TicketOrderResult;
import vn.edu.iuh.dto.admin.v1.req.CreateOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.RefundOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateCustomerInOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminOrderResponseDTO;
import vn.edu.iuh.dto.req.*;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.*;
import vn.edu.iuh.models.enums.*;
import vn.edu.iuh.projections.admin.v1.AdminOrderOverviewProjection;
import vn.edu.iuh.projections.admin.v1.AdminOrderProjection;
import vn.edu.iuh.projections.v1.OrderProjection;
import vn.edu.iuh.projections.v1.ProductProjection;
import vn.edu.iuh.projections.v1.TicketPriceLineProjection;
import vn.edu.iuh.repositories.*;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.OrderService;
import vn.edu.iuh.services.ProductService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.OrderSpecifications;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vn.edu.iuh.services.impl.RoomLayoutServiceImpl.convertToDayType;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ShowTimeRepository showTimeRepository;
    private final SeatRepository seatRepository;
    private final PromotionLineRepository promotionLineRepository;
    private final PromotionDetailRepository promotionDetailRepository;
    private final TicketPriceLineRepository ticketPriceLineRepository;
    private final StringRedisTemplate redisTemplate;
    private final ModelMapper modelMapper;

    public static final String ORDER_KEY_PREFIX = "order:";

    @Async
    public void saveOrderToRedis(UUID orderId) {
        String key = ORDER_KEY_PREFIX + orderId;
        redisTemplate.opsForValue().set(key, String.valueOf(orderId));
        redisTemplate.expire(key, 7, TimeUnit.MINUTES);
    }

    @Override
    public SuccessResponse<List<OrderProjection>> getOrderHistory(UserPrincipal userPrincipal) {
        List<OrderStatus> statuses = List.of(OrderStatus.COMPLETED, OrderStatus.CANCELLED);
        List<OrderProjection> orders = orderRepository.findAllByUserAndStatusIn(
                User.builder().id(userPrincipal.getId()).build(),
                statuses,
                OrderProjection.class
        );
        return new SuccessResponse<>(200, "success", "Thành công", orders);
    }

    @Override
    @Transactional
    public OrderProjection createOrder(UserPrincipal principal, OrderCreateRequestDTO dto) {
        return createNewOrder(
                dto.getShowTimeId(),
                principal.getId(),
                dto.getSeatIds(), OrderProjection.class
        );
    }

    @Override
    public AdminOrderProjection createOrderByEmployee(CreateOrderRequestDTO dto) {
        return createNewOrder(
                dto.getShowTimeId(),
                dto.getCustomerId(),
                dto.getSeatIds(),
                AdminOrderProjection.class
        );
    }

    private <T> T createNewOrder(UUID showTimeId, UUID customerId, List<Integer> seatIds, Class<T> projectionType) {
        ShowTime showTime = validateAndGetShowTime(showTimeId);

        Order order = buildOrder(showTime, customerId);

        TicketOrderResult result = addTicketsToOrder(order, seatIds, showTime);
        order = result.getOrder();

        order.setTotalPrice(result.getTotalPrice());
        order.setFinalAmount(result.getTotalPrice());
        order = orderRepository.save(order);

        applyPromotion(order);

        saveOrderToRedis(order.getId());
        return getOrderProjectionById(order.getId(), projectionType);
    }

    private ShowTime validateAndGetShowTime(UUID showTimeId) {
        return showTimeRepository.findByIdAndDeleted(showTimeId, false)
                                 .orElseThrow(() -> new DataNotFoundException("Không tìm thấy lịch chiếu"));
    }

    private List<Seat> validateAndGetSeats(List<Integer> seatIds) {
        List<Seat> seats = seatRepository.findAllByIdInAndStatusAndDeleted(seatIds, SeatStatus.ACTIVE, false);
        if (seats.isEmpty()) {
            throw new DataNotFoundException("Ghế không tồn tại");
        }

        if (seats.size() != seatIds.size()) {
            throw new DataNotFoundException("Ghế không tồn tại");
        }
        return seats;
    }

    private Map<SeatType, Float> getTicketPrices(ShowTime showTime) {
        DayType dayType = convertToDayType(showTime.getStartDate().getDayOfWeek());
        List<TicketPriceLineProjection> prices = ticketPriceLineRepository.findByDayTypeAndDateAndTimeAndDeleted(
                dayType.name(),
                showTime.getStartDate(),
                showTime.getStartTime(),
                false
        );

        if (prices.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy giá vé cho lịch chiếu này");
        }

        return prices.stream().collect(Collectors.toMap(
                TicketPriceLineProjection::getSeatType,
                TicketPriceLineProjection::getPrice
        ));
    }

    private Order buildOrder(ShowTime showTime, UUID customerId) {
        return Order.builder()
                    .code(generateOrderCode())
                    .user(customerId != null ? User.builder().id(customerId).build() : null)
                    .status(OrderStatus.PENDING)
                    .showTime(showTime)
                    .build();
    }

    @Override
    public Page<AdminOrderResponseDTO> getAllOrders(
            String code,
            OrderStatus status,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    ) {
        Page<Order> orders = orderRepository.findAll(
                OrderSpecifications.withFilters(code, status, fromDate, toDate)
                                   .and(GenericSpecifications.withDeleted(false)),
                pageable
        );
        return orders.map(order -> modelMapper.map(order, AdminOrderResponseDTO.class));
    }

    @Override
    public Order findByIdAndUser(UUID orderId, UUID userId) {
        User user = User.builder().id(userId).build();
        return orderRepository.findByIdAndUserAndDeleted(orderId, user, false)
                              .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
    }

    @Override
    public Order findById(UUID orderId) {
        return orderRepository.findByIdAndDeleted(orderId, false)
                              .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
    }

    @Override
    @Transactional
    public SuccessResponse<?> cancelOrder(UserPrincipal userPrincipal, UUID orderId) {
        Order order = findByIdAndUser(orderId, userPrincipal.getId());
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BadRequestException("Không thể hủy đơn hàng đã hoàn thành");
        }

        // Update promotion usage count
        PromotionDetail promotionDetail = order.getPromotionDetail();
        if (promotionDetail != null) {
            promotionDetail.setCurrentUsageCount(promotionDetail.getCurrentUsageCount() - 1);
            promotionDetailRepository.save(promotionDetail);
        }

        orderRepository.deleteByIdAndUser(orderId, User.builder().id(userPrincipal.getId()).build());
        return new SuccessResponse<>(200, "success", "Xóa đơn hàng thành công", null);
    }

    @Override
    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                                     .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BadRequestException("Không thể hủy đơn hàng đã hoàn thành");
        }

        PromotionDetail promotionDetail = order.getPromotionDetail();
        if (promotionDetail != null) {
            promotionDetail.setCurrentUsageCount(promotionDetail.getCurrentUsageCount() + 1);
            promotionDetailRepository.save(promotionDetail);
        }

        orderRepository.deleteById(orderId);
    }

    @Override
    @Transactional
    public SuccessResponse<OrderProjection> updateProductsInOrder(
            UserPrincipal userPrincipal,
            UUID orderId,
            OrderUpdateProductRequestDTO orderUpdateProductRequestDTO
    ) {
        Order order = findByIdAndUser(orderId, userPrincipal.getId());

        // Clear promotion
        PromotionDetail promotionDetail = order.getPromotionDetail();
        if (promotionDetail != null) {
            promotionDetail.setCurrentUsageCount(promotionDetail.getCurrentUsageCount() - 1);
            promotionDetailRepository.save(promotionDetail);
        }
        order.setPromotionDetail(null);
        order.setTotalDiscount(0);
        order.setFinalAmount(order.getTotalPrice());
        order.setPromotionLine(null);

        Map<Integer, OrderDetail> existingOrderDetails = order.getOrderDetails().stream()
                                                              .filter(od -> od.getType() == OrderDetailType.PRODUCT)
                                                              .collect(Collectors.toMap(
                                                                      od -> od.getProduct().getId(),
                                                                      Function.identity()
                                                              ));

        List<OrderDetail> updatedOrderDetails = new ArrayList<>(order.getOrderDetails());
        updatedOrderDetails.removeIf(od -> od.getType() == OrderDetailType.PRODUCT);

        float totalPrice = calculateTotalPriceByType(order, OrderDetailType.TICKET);

        for (OrderProductRequestDTO product : orderUpdateProductRequestDTO.getProducts()) {
            ProductProjection productProjection = productRepository.findWithPriceById(
                    ProductStatus.ACTIVE,
                    false,
                    product.getId()
            );

            OrderDetail orderDetail = existingOrderDetails.get(product.getId());

            if (orderDetail == null) {
                orderDetail = OrderDetail.builder()
                                         .price(productProjection.getPrice())
                                         .product(Product.builder().id(productProjection.getId()).build())
                                         .quantity(product.getQuantity())
                                         .type(OrderDetailType.PRODUCT)
                                         .order(order)
                                         .build();
            } else {
                orderDetail.setQuantity(product.getQuantity());
                existingOrderDetails.remove(product.getId());
            }

            orderDetail.setOrder(order);
            updatedOrderDetails.add(orderDetail);

            float productTotalPrice = productProjection.getPrice() * product.getQuantity();
            totalPrice += productTotalPrice;

        }
        order.getOrderDetails().clear();
        order.getOrderDetails().addAll(updatedOrderDetails);
        order.setTotalPrice(totalPrice);
        order.setFinalAmount(totalPrice);
        order = orderRepository.save(order);

        applyPromotion(order);

        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class)
                                                         .orElseThrow(() -> new DataNotFoundException(
                                                                 "Không tìm thấy đơn hàng"));
        return new SuccessResponse<>(201, "success", "Cập nhật sản phẩm thành công", orderProjection);
    }

    @Override
    public AdminOrderProjection updateProductsInOrderByEmployee(
            UUID orderId,
            OrderUpdateProductRequestDTO orderUpdateProductRequestDTO
    ) {
        Order order = orderRepository.findById(orderId)
                                     .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));

        // Clear promotion
        PromotionDetail promotionDetail = order.getPromotionDetail();
        if (promotionDetail != null) {
            promotionDetail.setCurrentUsageCount(promotionDetail.getCurrentUsageCount() + 1);
            promotionDetailRepository.save(promotionDetail);
        }
        order.setPromotionDetail(null);
        order.setTotalDiscount(0);
        order.setFinalAmount(order.getTotalPrice());
        order.setPromotionLine(null);

        Map<Integer, OrderDetail> existingOrderDetails = order.getOrderDetails().stream()
                                                              .filter(od -> od.getType() == OrderDetailType.PRODUCT)
                                                              .collect(Collectors.toMap(
                                                                      od -> od.getProduct().getId(),
                                                                      Function.identity()
                                                              ));

        List<OrderDetail> updatedOrderDetails = new ArrayList<>(order.getOrderDetails());
        updatedOrderDetails.removeIf(od -> od.getType() == OrderDetailType.PRODUCT);

        float totalPrice = calculateTotalPriceByType(order, OrderDetailType.TICKET);

        for (OrderProductRequestDTO product : orderUpdateProductRequestDTO.getProducts()) {
            ProductProjection productProjection = productRepository.findWithPriceById(
                    ProductStatus.ACTIVE,
                    false,
                    product.getId()
            );

            OrderDetail orderDetail = existingOrderDetails.get(product.getId());

            if (orderDetail == null) {
                orderDetail = OrderDetail.builder()
                                         .price(productProjection.getPrice())
                                         .product(Product.builder().id(productProjection.getId()).build())
                                         .quantity(product.getQuantity())
                                         .type(OrderDetailType.PRODUCT)
                                         .order(order)
                                         .build();
            } else {
                orderDetail.setQuantity(product.getQuantity());
                existingOrderDetails.remove(product.getId());
            }

            orderDetail.setOrder(order);
            updatedOrderDetails.add(orderDetail);

            float productTotalPrice = productProjection.getPrice() * product.getQuantity();
            totalPrice += productTotalPrice;

        }
        order.getOrderDetails().clear();
        order.getOrderDetails().addAll(updatedOrderDetails);
        order.setTotalPrice(totalPrice);
        order.setFinalAmount(totalPrice);
        order = orderRepository.save(order);

        applyPromotion(order);

        return orderRepository.findById(order.getId(), AdminOrderProjection.class)
                              .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
    }

    @Override
    public AdminOrderProjection updateCustomerInOrderByEmployee(UUID orderId, UpdateCustomerInOrderRequestDTO dto) {
        Order order = orderRepository.findByIdAndDeleted(orderId, false)
                                     .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));

        if (dto.getCustomerId() == null) {
            order.setUser(null);
        } else {
            User user = User.builder().id(dto.getCustomerId()).build();
            order.setUser(user);
        }
        orderRepository.save(order);
        return orderRepository.findById(orderId, AdminOrderProjection.class)
                              .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
    }

    @Override
    @Transactional
    public SuccessResponse<OrderProjection> updateSeatsInOrder(
            UserPrincipal userPrincipal,
            UUID orderId,
            OrderUpdateSeatRequestDTO orderUpdateSeatRequestDTO
    ) {
        Order order = findByIdAndUser(orderId, userPrincipal.getId());

        // Clear promotion
        PromotionDetail promotionDetail = order.getPromotionDetail();
        if (promotionDetail != null) {
            promotionDetail.setCurrentUsageCount(promotionDetail.getCurrentUsageCount() - 1);
            promotionDetailRepository.save(promotionDetail);
        }
        order.setPromotionDetail(null);
        order.setTotalDiscount(0);
        order.setFinalAmount(order.getTotalPrice());
        order.setPromotionLine(null);

        ShowTime showTime = order.getShowTime();
        DayType dayType = convertToDayType(showTime.getStartDate().getDayOfWeek());
        List<TicketPriceLineProjection> prices = ticketPriceLineRepository.findByDayTypeAndDateAndTimeAndDeleted(
                dayType.name(),
                showTime.getStartDate(),
                showTime.getStartTime(),
                false
        );

        if (prices.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy giá vé cho lịch chiếu này");
        }

        Map<SeatType, Float> priceMap = prices.stream().collect(Collectors.toMap(
                TicketPriceLineProjection::getSeatType,
                TicketPriceLineProjection::getPrice
        ));

        List<OrderDetail> updatedOrderDetails = new ArrayList<>(order.getOrderDetails());
        updatedOrderDetails.removeIf(od -> od.getType() == OrderDetailType.TICKET);

        float totalPrice = calculateTotalPriceByType(order, OrderDetailType.PRODUCT);

        List<Seat> seats = seatRepository.findAllById(orderUpdateSeatRequestDTO.getSeatIds());
        for (Seat seat : seats) {
            float price = priceMap.get(seat.getType());
            totalPrice += price;
            OrderDetail orderDetail = OrderDetail.builder()
                                                 .price(price)
                                                 .type(OrderDetailType.TICKET)
                                                 .seat(seat)
                                                 .order(order)
                                                 .build();

            updatedOrderDetails.add(orderDetail);
        }

        order.getOrderDetails().clear();
        order.getOrderDetails().addAll(updatedOrderDetails);
        order.setTotalPrice(totalPrice);
        order.setFinalAmount(totalPrice);
        order = orderRepository.save(order);

        applyPromotion(order);

        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class)
                                                         .orElseThrow(() -> new DataNotFoundException(
                                                                 "Không tìm thấy đơn hàng"));
        return new SuccessResponse<>(201, "success", "Cập nhật ghế thành công", orderProjection);
    }

    @Override
    public AdminOrderProjection updateSeatsInOrderByEmployee(
            UUID orderId,
            OrderUpdateSeatRequestDTO orderUpdateSeatRequestDTO
    ) {
        Order order = orderRepository.findById(orderId)
                                     .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));

        // Clear promotion
        PromotionDetail promotionDetail = order.getPromotionDetail();
        if (promotionDetail != null) {
            promotionDetail.setCurrentUsageCount(promotionDetail.getCurrentUsageCount() + 1);
            promotionDetailRepository.save(promotionDetail);
        }
        order.setPromotionDetail(null);
        order.setTotalDiscount(0);
        order.setFinalAmount(order.getTotalPrice());
        order.setPromotionLine(null);

        List<OrderDetail> updatedOrderDetails = new ArrayList<>(order.getOrderDetails());
        updatedOrderDetails.removeIf(od -> od.getType() == OrderDetailType.TICKET);

        float totalPrice = calculateTotalPriceByType(order, OrderDetailType.PRODUCT);

        ShowTime showTime = order.getShowTime();
        DayType dayType = convertToDayType(showTime.getStartDate().getDayOfWeek());
        List<TicketPriceLineProjection> prices = ticketPriceLineRepository.findByDayTypeAndDateAndTimeAndDeleted(
                dayType.name(),
                showTime.getStartDate(),
                showTime.getStartTime(),
                false
        );

        if (prices.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy giá vé cho lịch chiếu này");
        }

        Map<SeatType, Float> priceMap = prices.stream().collect(Collectors.toMap(
                TicketPriceLineProjection::getSeatType,
                TicketPriceLineProjection::getPrice
        ));

        List<Seat> seats = seatRepository.findAllById(orderUpdateSeatRequestDTO.getSeatIds());
        for (Seat seat : seats) {
            float price = priceMap.get(seat.getType());
            totalPrice += price;
            OrderDetail orderDetail = OrderDetail.builder()
                                                 .price(price)
                                                 .type(OrderDetailType.TICKET)
                                                 .seat(seat)
                                                 .order(order)
                                                 .build();

            updatedOrderDetails.add(orderDetail);
        }

        order.getOrderDetails().clear();
        order.getOrderDetails().addAll(updatedOrderDetails);
        order.setTotalPrice(totalPrice);
        order.setFinalAmount(totalPrice);
        order = orderRepository.save(order);

        applyPromotion(order);

        return getOrderProjectionById(orderId, AdminOrderProjection.class);
    }

    @Override
    public SuccessResponse<OrderProjection> updateDiscountInOrder(
            UserPrincipal userPrincipal,
            UUID orderId,
            OrderUpdateDiscountDTO orderUpdateDiscountDTO
    ) {
        Order order = this.findByIdAndUser(orderId, userPrincipal.getId());

        if (order.getPromotionLine() != null) {
            throw new BadRequestException("Đơn hàng đã áp mã giảm giá");
        }

        LocalDate currentDate = LocalDate.now();
        PromotionLine promotionLine = promotionLineRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndCode(
                                                                     currentDate,
                                                                     currentDate,
                                                                     orderUpdateDiscountDTO.getCode()
                                                             )
                                                             .orElseThrow(() -> new DataNotFoundException(
                                                                     "Mã giảm giá không tồn tại"));
        List<PromotionDetail> promotionDetails = promotionLine.getPromotionDetails();
        if (promotionDetails.isEmpty()) {
            throw new DataNotFoundException("Mã giảm giá không tồn tại");
        }

        switch (promotionLine.getType()) {
            case CASH_REBATE -> {
                PromotionDetail bestPromotion = findBestApplicablePromotionForPromotionCashRebate(
                        promotionDetails,
                        order
                );
                if (bestPromotion == null) {
                    throw new BadRequestException("Không đủ điều kiện áp mã");
                }
                order.setTotalDiscount(bestPromotion.getDiscountValue());
                order.setFinalAmount(order.getTotalPrice() - bestPromotion.getDiscountValue());
                order.setPromotionLine(promotionLine);

                bestPromotion.setCurrentUsageCount(bestPromotion.getCurrentUsageCount() + 1);
                promotionDetailRepository.save(bestPromotion);
            }
            case PRICE_DISCOUNT -> {
                PromotionDetail bestPromotion = findBestApplicablePromotionForPromotionPriceDiscount(
                        promotionDetails,
                        order
                );
                if (bestPromotion == null) {
                    throw new BadRequestException("Không đủ điều kiện áp mã");
                }
                float discountValue = calculatorDiscount(order.getTotalPrice(), bestPromotion);
                order.setTotalDiscount(discountValue);
                order.setFinalAmount(order.getTotalPrice() - discountValue);
                order.setPromotionLine(promotionLine);

                bestPromotion.setCurrentUsageCount(bestPromotion.getCurrentUsageCount() + 1);
                promotionDetailRepository.save(bestPromotion);
            }
            case BUY_PRODUCTS_GET_PRODUCTS -> {

            }
            case BUY_PRODUCTS_GET_TICKETS -> {
            }
            case BUY_TICKETS_GET_TICKETS -> {
            }
            case BUY_TICKETS_GET_PRODUCTS -> {
                Map<SeatType, Integer> seatTypeQuantity = calculateSeatTypeQuantity(order.getOrderDetails());
                PromotionDetail bestPromotion = null;
                float highestDiscountValue = 0;
                List<ProductProjection> productProjections = productService.getProducts();

                for (PromotionDetail promotionDetail : promotionDetails) {
                    if (promotionDetail.getStatus() == BaseStatus.ACTIVE
                            && promotionDetail.getCurrentUsageCount() < promotionDetail.getUsageLimit()
                    ) {
                        boolean isApplicable = true;
                        for (Map.Entry<SeatType, Integer> entry : seatTypeQuantity.entrySet()) {
                            if (entry.getKey() == promotionDetail.getRequiredSeatType()
                                    && entry.getValue() < promotionDetail.getRequiredSeatQuantity()
                            ) {
                                isApplicable = false;
                                break;
                            }
                        }
                        if (isApplicable) {
                            float discountValue = productProjections.stream()
                                                                    .filter(productProjection -> promotionDetail.getGiftProduct()
                                                                                                                .getId() == productProjection.getId())
                                                                    .map(ProductProjection::getPrice)
                                                                    .findFirst()
                                                                    .orElse(0f);
                            if (discountValue > highestDiscountValue) {
                                highestDiscountValue = discountValue;
                                bestPromotion = promotionDetail;
                            }
                        }
                    }
                }

                if (bestPromotion != null) {
                    List<OrderDetail> orderDetails = order.getOrderDetails();
                    boolean isApplicable = false;
                    for (OrderDetail orderDetail : orderDetails) {
                        if (orderDetail.getType() == OrderDetailType.PRODUCT
                                && orderDetail.getProduct().getId() == bestPromotion.getGiftProduct().getId()
                        ) {
                            if (orderDetail.getQuantity() == bestPromotion.getGiftQuantity()) {
                                orderDetail.setGift(true);
                                orderDetail.setPrice(0);

                                order.setTotalDiscount(highestDiscountValue);
                                order.setTotalPrice(order.getTotalPrice() - highestDiscountValue);
                                order.setFinalAmount(order.getFinalAmount() - highestDiscountValue);
                                log.info("toi day");
                                isApplicable = true;
                                break;

                            } else if (orderDetail.getQuantity() < bestPromotion.getGiftQuantity()) {
                                float oldPrice = orderDetail.getPrice() * orderDetail.getQuantity();
                                orderDetail.setQuantity(bestPromotion.getGiftQuantity());
                                orderDetail.setGift(true);
                                orderDetail.setPrice(0);

                                order.setTotalDiscount(highestDiscountValue);
                                order.setTotalPrice(order.getTotalPrice() - oldPrice);
                                order.setFinalAmount(order.getFinalAmount() - oldPrice);
                                log.info("toi day 2");
                                isApplicable = true;
                                break;

                            } else {
                                orderDetail.setQuantity(orderDetail.getQuantity() - bestPromotion.getGiftQuantity());
                                OrderDetail giftOrderDetail = OrderDetail.builder()
                                                                         .price(0)
                                                                         .isGift(true)
                                                                         .product(bestPromotion.getGiftProduct())
                                                                         .quantity(bestPromotion.getGiftQuantity())
                                                                         .type(OrderDetailType.PRODUCT)
                                                                         .order(order)
                                                                         .build();

                                orderDetails.add(giftOrderDetail);

                                order.setTotalDiscount(highestDiscountValue);
                                order.setTotalPrice(order.getTotalPrice() - highestDiscountValue);
                                order.setFinalAmount(order.getFinalAmount() - highestDiscountValue);

                                log.info("toi day 3");
                                isApplicable = true;
                                break;
                            }
                        }
                    }

                    if (!isApplicable) {
                        OrderDetail giftOrderDetail = OrderDetail.builder()
                                                                 .price(0)
                                                                 .product(bestPromotion.getGiftProduct())
                                                                 .quantity(bestPromotion.getGiftQuantity())
                                                                 .type(OrderDetailType.PRODUCT)
                                                                 .order(order)
                                                                 .build();

                        orderDetails.add(giftOrderDetail);

                        order.setTotalDiscount(highestDiscountValue);
                        log.info("toi day 4");
                    }

                    order.setOrderDetails(orderDetails);
                    order.setPromotionLine(promotionLine);
                    orderRepository.save(order);
                } else {
                    throw new BadRequestException("Không đủ điều kiện áp mã");
                }
            }
            default -> throw new DataNotFoundException("Không tìm thấy mã giảm giá");
        }
        orderRepository.save(order);

        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class)
                                                         .orElseThrow(() -> new DataNotFoundException(
                                                                 "Không tìm thấy đơn hàng"));
        return new SuccessResponse<>(201, "success", "Áp khuyến mãi thành công", orderProjection);
    }

    @Override
    public SuccessResponse<OrderProjection> clearDiscountInOrder(UserPrincipal userPrincipal, UUID orderId) {
        Order order = this.findByIdAndUser(orderId, userPrincipal.getId());


        order.setTotalDiscount(0);
        order.setFinalAmount(order.getTotalPrice());
        order.setPromotionLine(null);
        orderRepository.save(order);

        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class)
                                                         .orElseThrow(() -> new DataNotFoundException(
                                                                 "Không tìm thấy đơn hàng"));
        return new SuccessResponse<>(201, "success", "Xóa khuyến mãi thành công", orderProjection);
    }

    @Override
    public AdminOrderOverviewProjection getOrderByCode(String code) {
        return orderRepository.findByCode(code, AdminOrderOverviewProjection.class)
                              .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
    }

    @Transactional
    @Override
    public void refundOrder(UUID orderId, RefundOrderRequestDTO refundOrderRequestDTO) {
        Order order = orderRepository.findByIdAndDeletedAndStatus(orderId, false, OrderStatus.COMPLETED)
                                     .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));

        ShowTime showTime = order.getShowTime();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime showDateTime = LocalDateTime.of(showTime.getStartDate(), showTime.getStartTime());
        Duration timeUntilShow = Duration.between(now, showDateTime);

        if (timeUntilShow.toMillis() < 0) {
            throw new BadRequestException("Không thể hoàn tiền cho đơn hàng đã qua suất chiếu");
        }

        order.setStatus(OrderStatus.CANCELLED);

        PromotionDetail promotionDetail = order.getPromotionDetail();
        if (promotionDetail != null) {
            promotionDetail.setCurrentUsageCount(promotionDetail.getCurrentUsageCount() - 1);
            promotionDetailRepository.save(promotionDetail);
        }

        Refund refund = Refund.builder()
                              .order(order)
                              .code(generateRefundCode())
                              .amount(order.getFinalAmount())
                              .refundMethod(RefundMethod.CASH)
                              .reason(refundOrderRequestDTO.getReason())
                              .refundDate(LocalDateTime.now())
                              .status(RefundStatus.PENDING)
                              .build();
        refundRepository.save(refund);
        orderRepository.save(order);
    }

    @Override
    public AdminOrderProjection completeOrder(UUID orderId) {
        Order order = findById(orderId);
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        return getOrderProjectionById(orderId, AdminOrderProjection.class);
    }

    @Override
    public OrderProjection completeOrder(UserPrincipal principal, UUID orderId) {
        Order order = findByIdAndUser(orderId, principal.getId());
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        return getOrderProjectionById(orderId, OrderProjection.class);
    }

    private String generateOrderCode() {
        long orderCount = orderRepository.count();
        LocalDateTime now = LocalDateTime.now();
        return String.format("HD%s%06d", now.format(DateTimeFormatter.ofPattern("yyMMdd")), (orderCount % 1000000) + 1);
    }

    private String generateRefundCode() {
        long refundCount = refundRepository.count();
        return String.format("HDHT%08d", (refundCount % 100000000) + 1);
    }

    private float calculateTotalPriceByType(Order order, OrderDetailType type) {
        return order.getOrderDetails().stream()
                    .filter(od -> od.getType() == type)
                    .map(OrderDetail::getPrice)
                    .reduce(0f, Float::sum);
    }

    private PromotionDetail findBestApplicablePromotionForPromotionCashRebate(
            List<PromotionDetail> promotionDetails,
            Order order
    ) {
        float totalPrice = order.getTotalPrice();
        return promotionDetails.stream()
                               .filter(detail -> totalPrice >= detail.getMinOrderValue()
                                       && detail.getStatus() == BaseStatus.ACTIVE
                                       && detail.getCurrentUsageCount() < detail.getUsageLimit()
                               )
                               .max(Comparator.comparing(PromotionDetail::getDiscountValue))
                               .orElse(null);
    }

    private PromotionDetail findBestApplicablePromotionForPromotionPriceDiscount(
            List<PromotionDetail> promotionDetails,
            Order order
    ) {
        float totalPrice = order.getTotalPrice();
        return promotionDetails.stream()
                               .filter(detail -> totalPrice >= detail.getMinOrderValue()
                                       && detail.getStatus() == BaseStatus.ACTIVE
                                       && detail.getCurrentUsageCount() < detail.getUsageLimit()
                               )
                               .max(Comparator.comparing(detail -> calculatorDiscount(totalPrice, detail)))
                               .orElse(null);
    }

    private float calculatorDiscount(float totalPrice, PromotionDetail detail) {
        float discountValue = totalPrice * (detail.getDiscountValue() / 100);
        return Math.min(discountValue, detail.getMaxDiscountValue());
    }

    private Map<SeatType, Integer> calculateSeatTypeQuantity(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                           .filter(od -> od.getType() == OrderDetailType.TICKET && od.getSeat() != null)
                           .collect(Collectors.groupingBy(
                                   orderDetail -> orderDetail.getSeat().getType(),
                                   Collectors.summingInt(od -> 1)
                           ));

    }


    /**
     * Apply promotion for order
     */
    private void applyPromotion(Order order) {
        List<PromotionLine> promotionLines = promotionLineRepository.findActivePromotionLine(LocalDate.now());

        if (promotionLines.isEmpty()) {
            return;
        }

        PromotionDetail bestPromotion = null;
        float highestDiscountValue = 0;
        PromotionLine selectedPromotionLine = null;

        for (PromotionLine promotionLine : promotionLines) {
            switch (promotionLine.getType()) {
                case CASH_REBATE -> {
                    PromotionDetail promotionDetail = findBestApplicablePromotionForPromotionCashRebate(
                            promotionLine.getPromotionDetails(),
                            order
                    );
                    if (promotionDetail != null && promotionDetail.getDiscountValue() > highestDiscountValue) {
                        bestPromotion = promotionDetail;
                        highestDiscountValue = promotionDetail.getDiscountValue();
                        selectedPromotionLine = promotionLine;
                    }
                }
                case PRICE_DISCOUNT -> {
                    PromotionDetail promotionDetail = findBestApplicablePromotionForPromotionPriceDiscount(
                            promotionLine.getPromotionDetails(),
                            order
                    );

                    if (promotionDetail != null) {
                        float discountValue = calculatorDiscount(order.getTotalPrice(), promotionDetail);
                        if (discountValue > highestDiscountValue) {
                            bestPromotion = promotionDetail;
                            highestDiscountValue = discountValue;
                            selectedPromotionLine = promotionLine;
                        }
                    }
                }
            }
        }

        if (bestPromotion != null) {
            order.setTotalDiscount(highestDiscountValue);
            order.setFinalAmount(order.getTotalPrice() - highestDiscountValue);
            order.setPromotionDetail(bestPromotion);
            order.setPromotionLine(selectedPromotionLine);
            orderRepository.save(order);

            bestPromotion.setCurrentUsageCount(bestPromotion.getCurrentUsageCount() + 1);
            promotionDetailRepository.save(bestPromotion);
        }
    }

    private TicketOrderResult addTicketsToOrder(Order order, List<Integer> seatIds, ShowTime showTime) {
        List<Seat> seats = validateAndGetSeats(seatIds);

        Map<SeatType, Float> priceMap = getTicketPrices(showTime);

        List<OrderDetail> orderDetails = new ArrayList<>();
        float totalPrice = 0;

        for (Seat seat : seats) {
            float price = priceMap.get(seat.getType());
            totalPrice += price;

            OrderDetail orderDetail = buildTicket(seat, price, order);
            orderDetails.add(orderDetail);
        }

        order.setOrderDetails(orderDetails);
        return new TicketOrderResult(order, totalPrice);
    }

    private OrderDetail buildTicket(Seat seat, float price, Order order) {
        return OrderDetail.builder()
                          .price(price)
                          .type(OrderDetailType.TICKET)
                          .seat(seat)
                          .order(order)
                          .build();
    }

    private <T> T getOrderProjectionById(UUID orderId, Class<T> projectionType) {
        return orderRepository.findById(orderId, projectionType)
                              .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
    }
}
