package vn.edu.iuh.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.req.*;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.*;
import vn.edu.iuh.models.enums.*;
import vn.edu.iuh.projections.admin.v1.AdminOrderOverviewProjection;
import vn.edu.iuh.projections.admin.v1.BaseOrderProjection;
import vn.edu.iuh.projections.v1.OrderProjection;
import vn.edu.iuh.projections.v1.ProductProjection;
import vn.edu.iuh.projections.v1.TicketPriceLineProjection;
import vn.edu.iuh.repositories.*;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.OrderService;
import vn.edu.iuh.services.ProductService;

import java.time.LocalDate;
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
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final ShowTimeRepository showTimeRepository;
    private final SeatRepository seatRepository;
    private final PromotionLineRepository promotionLineRepository;
    private final PromotionDetailRepository promotionDetailRepository;
    private final TicketPriceLineRepository ticketPriceLineRepository;
    private final StringRedisTemplate redisTemplate;

    public static final String ORDER_KEY_PREFIX = "order:";

    @Async
    public void saveOrderToRedis(UUID orderId) {
        String key = ORDER_KEY_PREFIX + orderId;
        redisTemplate.opsForValue().set(key, String.valueOf(orderId));
        redisTemplate.expire(key, 7, TimeUnit.MINUTES);
    }

    @Override
    public Order findByIdAndUser(UUID orderId, User user) {
        return orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
    }

    @Override
    public SuccessResponse<List<OrderProjection>> getOrderHistory(UserPrincipal userPrincipal) {
        List<OrderProjection> orders = orderRepository.findAllByUserAndStatus(User.builder().id(userPrincipal.getId()).build(), OrderStatus.COMPLETED, OrderProjection.class);
        return new SuccessResponse<>(200, "success", "Thành công", orders);
    }

    @Override
    @Transactional
    public SuccessResponse<OrderProjection> createOrder(UserPrincipal userPrincipal, OrderCreateRequestDTO orderCreateRequestDTO) {
        UUID showTimeId = orderCreateRequestDTO.getShowTimeId();
        ShowTime showTime = showTimeRepository.findById(showTimeId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy lịch chiếu có ID là " + showTimeId));
        List<Seat> seats = seatRepository.findAllByIdInAndStatus(orderCreateRequestDTO.getSeatIds(), SeatStatus.ACTIVE);

        if (seats.isEmpty()) {
            throw new DataNotFoundException("Ghế không tồn tại");
        }

        if (seats.size() != orderCreateRequestDTO.getSeatIds().size()) {
            throw new DataNotFoundException("Ghế không tồn tại");
        }

        DayType dayType = convertToDayType(showTime.getStartDate().getDayOfWeek());
        List<TicketPriceLineProjection> prices = ticketPriceLineRepository.findByDayTypeAndDateAndTime(dayType.name(), showTime.getStartDate(), showTime.getStartTime());

        if (prices.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy giá vé cho lịch chiếu này");
        }

        Map<SeatType, Float> priceMap = prices.stream().collect(Collectors.toMap(
                TicketPriceLineProjection::getSeatType,
                TicketPriceLineProjection::getPrice
        ));

        Order order = Order.builder()
                .user(User.builder().id(userPrincipal.getId()).build())
                .code(generateOrderCode())
                .status(OrderStatus.PENDING)
                .showTime(showTime)
                .build();

        List<OrderDetail> orderDetails = new ArrayList<>();
        float totalPrice = 0;

        for (Seat seat : seats) {
            float price = priceMap.get(seat.getType());
            totalPrice += price;
            OrderDetail orderDetail = OrderDetail.builder()
                    .price(price)
                    .type(OrderDetailType.TICKET)
                    .seat(seat)
                    .order(order)
                    .build();

            orderDetails.add(orderDetail);
        }

        order.setTotalPrice(totalPrice);
        order.setFinalAmount(totalPrice);
        order.setOrderDetails(orderDetails);
        order = orderRepository.save(order);
        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        saveOrderToRedis(order.getId());
        return new SuccessResponse<>(201, "success", "Tạo đơn hàng thành công", orderProjection);
    }

    @Override
    @Transactional
    public SuccessResponse<?> cancelOrder(UserPrincipal userPrincipal, UUID orderId) {
        orderRepository.deleteByIdAndUser(orderId, User.builder().id(userPrincipal.getId()).build());
        return new SuccessResponse<>(200, "success", "Xóa đơn hàng thành công", null);
    }

    @Override
    public SuccessResponse<OrderProjection> completeOrder(UserPrincipal userPrincipal, UUID orderId) {
        Order order = this.findByIdAndUser(orderId, User.builder().id(userPrincipal.getId()).build());
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        return new SuccessResponse<>(200, "success", "Đặt vé thành công", orderProjection);
    }

    @Override
    @Transactional
    public SuccessResponse<OrderProjection> updateProductsInOrder(UserPrincipal userPrincipal, UUID orderId, OrderUpdateProductRequestDTO orderUpdateProductRequestDTO) {
        Order order = orderRepository.findByIdAndUser(orderId, User.builder().id(userPrincipal.getId()).build()).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));

        Map<Integer, OrderDetail> existingOrderDetails = order.getOrderDetails().stream()
                .filter(od -> od.getType() == OrderDetailType.PRODUCT)
                .collect(Collectors.toMap(od -> od.getProduct().getId(), Function.identity()));

        List<OrderDetail> updatedOrderDetails = new ArrayList<>(order.getOrderDetails());
        updatedOrderDetails.removeIf(od -> od.getType() == OrderDetailType.PRODUCT);

        float totalPrice = calculateTotalPriceByType(order, OrderDetailType.TICKET);

        for (OrderProductRequestDTO product : orderUpdateProductRequestDTO.getProducts()) {
            ProductProjection productProjection = productRepository.findWithPriceById(ProductStatus.ACTIVE, false, product.getId());

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

        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        return new SuccessResponse<>(201, "success", "Cập nhật sản phẩm thành công", orderProjection);
    }

    @Override
    @Transactional
    public SuccessResponse<OrderProjection> updateSeatsInOrder(UserPrincipal userPrincipal, UUID orderId, OrderUpdateSeatRequestDTO orderUpdateSeatRequestDTO) {
        Order order = this.findByIdAndUser(orderId, User.builder().id(userPrincipal.getId()).build());

        ShowTime showTime = order.getShowTime();
        DayType dayType = convertToDayType(showTime.getStartDate().getDayOfWeek());
        List<TicketPriceLineProjection> prices = ticketPriceLineRepository.findByDayTypeAndDateAndTime(dayType.name(), showTime.getStartDate(), showTime.getStartTime());

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

        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        return new SuccessResponse<>(201, "success", "Cập nhật ghế thành công", orderProjection);
    }

    @Override
    public SuccessResponse<OrderProjection> updateDiscountInOrder(UserPrincipal userPrincipal, UUID orderId, OrderUpdateDiscountDTO orderUpdateDiscountDTO) {
        Order order = this.findByIdAndUser(orderId, User.builder().id(userPrincipal.getId()).build());

        if (order.getPromotionLine() != null) {
            throw new BadRequestException("Đơn hàng đã áp mã giảm giá");
        }

        LocalDate currentDate = LocalDate.now();
        PromotionLine promotionLine = promotionLineRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndCode(currentDate, currentDate, orderUpdateDiscountDTO.getCode())
                .orElseThrow(() -> new DataNotFoundException("Mã giảm giá không tồn tại"));
        List<PromotionDetail> promotionDetails = promotionLine.getPromotionDetails();
        if (promotionDetails.isEmpty()) {
            throw new DataNotFoundException("Mã giảm giá không tồn tại");
        }

        switch (promotionLine.getType()) {
            case CASH_REBATE -> {
                PromotionDetail bestPromotion = findBestApplicablePromotionForPromotionCashRebate(promotionDetails, order);
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
                PromotionDetail bestPromotion = findBestApplicablePromotionForPromotionPriceDiscount(promotionDetails, order);
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
                                    .filter(productProjection -> promotionDetail.getGiftProduct().getId() == productProjection.getId())
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

        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        return new SuccessResponse<>(201, "success", "Áp khuyến mãi thành công", orderProjection);
    }

    @Override
    public SuccessResponse<OrderProjection> clearDiscountInOrder(UserPrincipal userPrincipal, UUID orderId) {
        Order order = this.findByIdAndUser(orderId, User.builder().id(userPrincipal.getId()).build());


        order.setTotalDiscount(0);
        order.setFinalAmount(order.getTotalPrice());
        order.setPromotionLine(null);
        orderRepository.save(order);

        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        return new SuccessResponse<>(201, "success", "Xóa khuyến mãi thành công", orderProjection);
    }

    @Override
    public Page<BaseOrderProjection> getAllOrders(Pageable pageable) {
        return orderRepository.findAllByStatusAndDeleted(OrderStatus.COMPLETED, false, pageable, BaseOrderProjection.class);
    }

    @Override
    public AdminOrderOverviewProjection getOrderByCode(String code) {
        return orderRepository.findByCode(code, AdminOrderOverviewProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
    }

    private String generateOrderCode() {
        long orderCount = orderRepository.count();
        return String.format("HD%08d", (orderCount % 100000000) + 1);
    }

    private float calculateTotalPriceByType(Order order, OrderDetailType type) {
        return order.getOrderDetails().stream()
                .filter(od -> od.getType() == type)
                .map(OrderDetail::getPrice)
                .reduce(0f, Float::sum);
    }

    private PromotionDetail findBestApplicablePromotionForPromotionCashRebate(List<PromotionDetail> promotionDetails, Order order) {
        float totalPrice = order.getTotalPrice();
        return promotionDetails.stream()
                .filter(detail -> totalPrice >= detail.getMinOrderValue()
                                  && detail.getStatus() == BaseStatus.ACTIVE
                                  && detail.getCurrentUsageCount() < detail.getUsageLimit()
                )
                .max(Comparator.comparing(PromotionDetail::getDiscountValue))
                .orElse(null);
    }

    private PromotionDetail findBestApplicablePromotionForPromotionPriceDiscount(List<PromotionDetail> promotionDetails, Order order) {
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
}
