package vn.edu.iuh.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.req.OrderCreateRequestDTO;
import vn.edu.iuh.dto.req.OrderProductRequestDTO;
import vn.edu.iuh.dto.req.OrderUpdateProductRequestDTO;
import vn.edu.iuh.dto.req.OrderUpdateSeatRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.*;
import vn.edu.iuh.models.enums.*;
import vn.edu.iuh.projections.v1.OrderProjection;
import vn.edu.iuh.projections.v1.ProductProjection;
import vn.edu.iuh.projections.v1.TicketPriceLineProjection;
import vn.edu.iuh.repositories.*;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vn.edu.iuh.services.impl.RoomLayoutServiceImpl.convertToDayType;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ShowTimeRepository showTimeRepository;
    private final SeatRepository seatRepository;
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
    public SuccessResponse<List<OrderProjection>> getOrderHistory(UserPrincipal userPrincipal) {
        List<OrderProjection> orders = orderRepository.findAllByUserAndStatus(User.builder().id(userPrincipal.getId()).build(), OrderStatus.COMPLETED, OrderProjection.class);
        return new SuccessResponse<>(200, "success", "Thành công", orders);
    }

    @Override
    @Transactional
    public SuccessResponse<OrderProjection> createOrder(UserPrincipal userPrincipal, OrderCreateRequestDTO orderCreateRequestDTO) {
        UUID showTimeId = orderCreateRequestDTO.getShowTimeId();
        ShowTime showTime = showTimeRepository.findById(showTimeId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy lịch chiếu có ID là " + showTimeId));

        DayType dayType = convertToDayType(showTime.getStartDate().getDayOfWeek());
        List<TicketPriceLineProjection> prices = ticketPriceLineRepository.findByDayTypeAndDateAndTime(dayType.name(), showTime.getStartDate(), showTime.getStartTime());

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

        List<Seat> seats = seatRepository.findAllById(orderCreateRequestDTO.getSeatIds());
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
        Order order = orderRepository.findByIdAndUser(orderId, User.builder().id(userPrincipal.getId()).build()).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
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
        order = orderRepository.save(order);

        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        return new SuccessResponse<>(201, "success", "Cập nhật sản phẩm thành công", orderProjection);
    }

    @Override
    @Transactional
    public SuccessResponse<OrderProjection> updateSeatsInOrder(UserPrincipal userPrincipal, UUID orderId, OrderUpdateSeatRequestDTO orderUpdateSeatRequestDTO) {
        Order order = orderRepository.findByIdAndUser(orderId, User.builder().id(userPrincipal.getId()).build()).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));

        ShowTime showTime = order.getShowTime();
        DayType dayType = convertToDayType(showTime.getStartDate().getDayOfWeek());
        List<TicketPriceLineProjection> prices = ticketPriceLineRepository.findByDayTypeAndDateAndTime(dayType.name(), showTime.getStartDate(), showTime.getStartTime());

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
        order = orderRepository.save(order);

        OrderProjection orderProjection = orderRepository.findById(order.getId(), OrderProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
        return new SuccessResponse<>(201, "success", "Cập nhật ghế ngồi thành công", orderProjection);
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
}
