package vn.edu.iuh.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
import vn.edu.iuh.exceptions.InternalServerErrorException;
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
    private static final int MAX_ATTEMPTS = 1000000;
    private static final int SEQUENCE_LENGTH = 6;
    private static final String DATE_PATTERN = "yyMMdd";
    private static final String ORDER_CODE_PREFIX = "HD";

    public static final String ORDER_KEY_PREFIX = "order:";
    private final UserRepository userRepository;

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
    public AdminOrderProjection updateOrderCustomer(UUID orderId, UpdateCustomerInOrderRequestDTO dto) {
        Order order = findById(orderId);
        UUID newCustomerId = dto.getCustomerId();

        if (newCustomerId != null && !userRepository.existsByIdAndDeleted(newCustomerId, false)) {
            throw new DataNotFoundException("Không tìm thấy khách hàng");
        }

        order.setUser(newCustomerId != null ? User.builder().id(dto.getCustomerId()).build() : null);
        orderRepository.save(order);
        return getOrderProjectionById(orderId, AdminOrderProjection.class);
    }

    @Override
    @Transactional
    public OrderProjection updateOrderProductsByCustomer(
            UserPrincipal principal,
            UUID orderId,
            OrderUpdateProductRequestDTO request
    ) {
        Order order = findByIdAndUser(orderId, principal.getId());
        updateOrderProducts(order, request);
        return getOrderProjectionById(orderId, OrderProjection.class);
    }

    @Override
    public AdminOrderProjection updateOrderProductsByEmployee(
            UUID orderId,
            OrderUpdateProductRequestDTO request
    ) {
        Order order = findById(orderId);
        updateOrderProducts(order, request);
        return getOrderProjectionById(orderId, AdminOrderProjection.class);
    }

    private void updateOrderProducts(Order order, OrderUpdateProductRequestDTO request) {
        clearPromotionFromOrder(order);

        List<OrderDetail> updatedOrderDetails = order.getOrderDetails().stream()
                                                     .filter(od -> od.getType() == OrderDetailType.TICKET || (od.getType() == OrderDetailType.PRODUCT && od.isGift()))
                                                     .collect(Collectors.toList());

        float totalPrice = calculateTotalPriceByType(order, OrderDetailType.TICKET);

        Map<Integer, OrderDetail> existingOrderDetails = order.getOrderDetails().stream()
                                                              .filter(od -> od.getType() == OrderDetailType.PRODUCT && !od.isGift())
                                                              .collect(Collectors.toMap(
                                                                      od -> od.getProduct().getId(),
                                                                      Function.identity()
                                                              ));

        for (OrderProductRequestDTO product : request.getProducts()) {
            OrderDetail orderDetail = processProductUpdate(
                    product,
                    existingOrderDetails,
                    order
            );

            updatedOrderDetails.add(orderDetail);
            totalPrice += orderDetail.getPrice() * orderDetail.getQuantity();
        }
        updateOrderWithNewDetails(order, updatedOrderDetails, totalPrice);
        applyPromotion(order);
    }

    private void updateOrderWithNewDetails(Order order, List<OrderDetail> newDetails, float totalPrice) {
        order.getOrderDetails().clear();
        order.getOrderDetails().addAll(newDetails);
        order.setTotalPrice(totalPrice);
        order.setFinalAmount(totalPrice);
        orderRepository.save(order);
    }

    private OrderDetail processProductUpdate(
            OrderProductRequestDTO product,
            Map<Integer, OrderDetail> existingDetails,
            Order order
    ) {
        ProductProjection productInfo = productRepository.findWithPriceById(
                ProductStatus.ACTIVE,
                false,
                product.getId()
        );

        OrderDetail orderDetail = existingDetails.get(product.getId());

        if (orderDetail == null) {
            return OrderDetail.builder()
                              .price(productInfo.getPrice())
                              .product(Product.builder().id(productInfo.getId()).build())
                              .quantity(product.getQuantity())
                              .type(OrderDetailType.PRODUCT)
                              .order(order)
                              .build();
        } else {
            orderDetail.setQuantity(product.getQuantity());
            existingDetails.remove(product.getId());
            return orderDetail;
        }
    }

    @Override
    @Transactional
    public OrderProjection updateOrderSeatsByCustomer(
            UserPrincipal principal,
            UUID orderId,
            OrderUpdateSeatRequestDTO request
    ) {
        Order order = findByIdAndUser(orderId, principal.getId());
        updateOrderSeats(order, request.getSeatIds());
        return getOrderProjectionById(orderId, OrderProjection.class);
    }

    @Override
    public AdminOrderProjection updateOrderSeatsByEmployee(
            UUID orderId,
            OrderUpdateSeatRequestDTO request
    ) {
        Order order = findById(orderId);
        updateOrderSeats(order, request.getSeatIds());
        return getOrderProjectionById(orderId, AdminOrderProjection.class);
    }

    /**
     * Core logic for updating seats in an order
     */
    private void updateOrderSeats(Order order, List<Integer> seatIds) {
        clearPromotionFromOrder(order);

        Map<SeatType, Float> priceMap = getTicketPrices(order.getShowTime());

        List<OrderDetail> updatedOrderDetails = order.getOrderDetails().stream()
                                                     .filter(od -> od.getType() != OrderDetailType.TICKET)
                                                     .collect(Collectors.toList());

        float totalPrice = calculateTotalPriceByType(order, OrderDetailType.PRODUCT);

        List<OrderDetail> seatDetails = createSeatOrderDetails(seatIds, priceMap, order, totalPrice);
        updatedOrderDetails.addAll(seatDetails);

        updateOrderWithNewDetails(order, updatedOrderDetails, totalPrice);
        applyPromotion(order);
    }

    private List<OrderDetail> createSeatOrderDetails(
            List<Integer> seatIds,
            Map<SeatType, Float> priceMap,
            Order order,
            float totalPrice
    ) {
        List<Seat> seats = validateAndGetSeats(seatIds);
        List<OrderDetail> seatDetails = new ArrayList<>();
        for (Seat seat : seats) {
            float price = priceMap.get(seat.getType());
            totalPrice += price;
            OrderDetail orderDetail = buildTicket(seat, price, order);
            seatDetails.add(orderDetail);
        }
        return seatDetails;
    }

    @Override
    public SuccessResponse<OrderProjection> updateDiscountInOrder(
            UserPrincipal principal,
            UUID orderId,
            OrderUpdateDiscountDTO request
    ) {
        Order order = this.findByIdAndUser(orderId, principal.getId());

        if (order.getPromotionLine() != null) {
            throw new BadRequestException("Đơn hàng đã áp mã giảm giá");
        }

        LocalDate currentDate = LocalDate.now();
        PromotionLine promotionLine = promotionLineRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndCode(
                                                                     currentDate,
                                                                     currentDate,
                                                                     request.getCode()
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
    public void refundOrder(UUID orderId, RefundOrderRequestDTO request) {
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
                              .reason(request.getReason())
                              .refundDate(LocalDateTime.now())
                              .status(RefundStatus.COMPLETED)
                              .build();
        refundRepository.save(refund);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrderByCustomer(UserPrincipal principal, UUID orderId) {
        Order order = findByIdAndUser(orderId, principal.getId());
        cancelOrder(order);
    }

    @Override
    public void cancelOrderByEmployee(UUID orderId) {
        Order order = findById(orderId);
        cancelOrder(order);
    }

    private void cancelOrder(Order order) {
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BadRequestException("Không thể hủy đơn hàng đã hoàn thành");
        }

        PromotionDetail promotionDetail = order.getPromotionDetail();
        if (promotionDetail != null) {
            promotionDetail.setCurrentUsageCount(promotionDetail.getCurrentUsageCount() - 1);
            promotionDetailRepository.save(promotionDetail);
        }

        orderRepository.deleteById(order.getId());
    }

    @Override
    public AdminOrderProjection completeOrder(UUID orderId) {
        Order order = findById(orderId);
        order.setStatus(OrderStatus.COMPLETED);
        order.setPaymentMethod(PaymentMethod.CASH);
        order.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order);
        return getOrderProjectionById(orderId, AdminOrderProjection.class);
    }

    @Override
    public OrderProjection completeOrder(UserPrincipal principal, UUID orderId) {
        Order order = findByIdAndUser(orderId, principal.getId());
        order.setStatus(OrderStatus.COMPLETED);
        order.setPaymentMethod(PaymentMethod.CASH);
        order.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order);
        return getOrderProjectionById(orderId, OrderProjection.class);
    }

    private String generateOrderCode() {
        long initialCount = orderRepository.count();
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyMMdd"));

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            long sequenceNumber = ((initialCount + attempt) % 1000000) + 1;
            String code = String.format("%s%s%06d", ORDER_CODE_PREFIX, dateStr, sequenceNumber);
            if (!orderRepository.existsByCode(code)) {
                return code;
            }
        }
        throw new InternalServerErrorException("Không thể tạo mã đơn hàng");
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

    private PromotionDetail findBestApplicablePromotionForPromotionBuyTicketsGetProducts(
            List<PromotionDetail> promotionDetails,
            Order order
    ) {
        Map<SeatType, Integer> seatTypeQuantity = calculateSeatTypeQuantity(order.getOrderDetails());
        List<ProductProjection> products = productService.getProducts();
        return promotionDetails.stream()
                               .filter(this::isPromotionDetailValid)
                               .filter(detail -> meetsRequiredSeatQuantity(seatTypeQuantity, detail))
                               .max(Comparator.comparing(detail -> calculatePromotionValue(detail, products)))
                               .orElse(null);
    }

    private float calculatePromotionValue(PromotionDetail detail, List<ProductProjection> products) {
        return products.stream()
                       .filter(product -> product.getId() == detail.getGiftProduct().getId())
                       .findFirst()
                       .map(product -> product.getPrice() * detail.getGiftQuantity())
                       .orElse(0f);
    }

    private boolean isPromotionDetailValid(PromotionDetail detail) {
        return detail.getStatus() == BaseStatus.ACTIVE
                && detail.getCurrentUsageCount() < detail.getUsageLimit();
    }

    private boolean meetsRequiredSeatQuantity(Map<SeatType, Integer> seatTypeQuantity, PromotionDetail detail) {
        return seatTypeQuantity.getOrDefault(detail.getRequiredSeatType(), 0) >= detail.getRequiredSeatQuantity();
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

    private void clearPromotionFromOrder(Order order) {
        PromotionDetail promotionDetail = order.getPromotionDetail();
        if (promotionDetail != null) {
            promotionDetail.setCurrentUsageCount(promotionDetail.getCurrentUsageCount() - 1);
            promotionDetailRepository.save(promotionDetail);
        }
        order.setPromotionDetail(null);
        order.setTotalDiscount(0);
        order.setFinalAmount(order.getTotalPrice());
        order.setPromotionLine(null);
    }


    /**
     * Apply promotion for order
     */
    private void applyPromotion(Order order) {
        List<PromotionLine> promotionLines = promotionLineRepository.findActivePromotionLine(LocalDate.now());

        if (promotionLines.isEmpty()) {
            return;
        }

        BestPromotionResult bestResult = findBestPromotionResult(order, promotionLines);

        if (bestResult != null) {
            switch (bestResult.getPromotionLine().getType()) {
                case CASH_REBATE, PRICE_DISCOUNT -> applyCashDiscount(order, bestResult);
                case BUY_TICKETS_GET_PRODUCTS -> applyProductGift(order, bestResult);
            }
            orderRepository.save(order);
            updatePromotionUsage(bestResult.getPromotionDetail());
        }
    }

    private BestPromotionResult findBestPromotionResult(Order order, List<PromotionLine> promotionLines) {
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
                case BUY_TICKETS_GET_PRODUCTS -> {
                    PromotionDetail promotionDetail = findBestApplicablePromotionForPromotionBuyTicketsGetProducts(
                            promotionLine.getPromotionDetails(),
                            order
                    );


                    if (promotionDetail != null) {
                        float giftValue = calculateGiftProductValue(promotionDetail);
                        if (giftValue > highestDiscountValue) {
                            bestPromotion = promotionDetail;
                            highestDiscountValue = giftValue;
                            selectedPromotionLine = promotionLine;
                        }
                    }
                }
            }
        }
        return bestPromotion != null ? new BestPromotionResult(selectedPromotionLine, bestPromotion, highestDiscountValue) : null;
    }

    private void applyCashDiscount(Order order, BestPromotionResult result) {
        order.setTotalDiscount(result.getDiscountValue());
        order.setFinalAmount(order.getTotalPrice() - result.getDiscountValue());
        order.setPromotionDetail(result.getPromotionDetail());
        order.setPromotionLine(result.getPromotionLine());
        orderRepository.save(order);
    }

    private void applyProductGift(Order order, BestPromotionResult result) {
        PromotionDetail detail = result.getPromotionDetail();

        Optional<OrderDetail> existingProduct = order.getOrderDetails().stream()
                .filter(od -> od.getType() == OrderDetailType.PRODUCT
                        && od.getProduct().getId() == detail.getGiftProduct().getId())
                .findFirst();


        if (existingProduct.isPresent()) {
            OrderDetail productDetail = existingProduct.get();

            if (productDetail.getQuantity() <= detail.getGiftQuantity()) {
                productDetail.setGift(true);
                productDetail.setPrice(0);
                productDetail.setQuantity(detail.getGiftQuantity());
            } else {
                productDetail.setQuantity(productDetail.getQuantity() - detail.getGiftQuantity());
                OrderDetail giftDetail = OrderDetail.builder()
                        .product(detail.getGiftProduct())
                        .quantity(detail.getGiftQuantity())
                        .price(0)
                        .type(OrderDetailType.PRODUCT)
                        .order(order)
                        .build();
                order.getOrderDetails().add(giftDetail);
            }
        } else {
            OrderDetail giftDetail = OrderDetail.builder()
                    .product(detail.getGiftProduct())
                    .quantity(detail.getGiftQuantity())
                    .price(0)
                    .type(OrderDetailType.PRODUCT)
                    .isGift(true)
                    .order(order)
                    .build();
            order.getOrderDetails().add(giftDetail);
        }

        order.setTotalDiscount(0);
        order.setPromotionDetail(detail);
        order.setPromotionLine(result.getPromotionLine());
    }

    @Async
    public void updatePromotionUsage(PromotionDetail detail) {
        detail.setCurrentUsageCount(detail.getCurrentUsageCount() + 1);
        promotionDetailRepository.save(detail);
    }


    private float calculateGiftProductValue(PromotionDetail promotionDetail) {
        List<ProductProjection> products = productService.getProducts();
        return products.stream()
                       .filter(product -> Objects.equals(product.getId(), promotionDetail.getGiftProduct().getId()))
                       .findFirst()
                       .map(product -> product.getPrice() * promotionDetail.getGiftQuantity())
                       .orElse(0f);
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

    @Getter
    @AllArgsConstructor
    private static class BestPromotionResult {
        private final PromotionLine promotionLine;
        private final PromotionDetail promotionDetail;
        private final float discountValue;
    }
}
