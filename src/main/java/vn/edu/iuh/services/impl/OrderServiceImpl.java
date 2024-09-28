package vn.edu.iuh.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.req.OrderRequestDTO;
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
import java.util.stream.Collectors;

import static vn.edu.iuh.services.impl.RoomLayoutServiceImpl.convertToDayType;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ShowTimeRepository showTimeRepository;
    private final SeatRepository seatRepository;
    private final TicketPriceLineRepository ticketPriceLineRepository;

    @Override
    public SuccessResponse<List<OrderProjection>> getOrderHistory(UserPrincipal userPrincipal) {
        List<OrderProjection> orders = orderRepository.findAllByUser(User.builder().id(userPrincipal.getId()).build(), OrderProjection.class);
        return new SuccessResponse<>(200, "success", "Thành công", orders);
    }

    @Override
    @Transactional
    public SuccessResponse<?> createOrder(UserPrincipal userPrincipal, OrderRequestDTO orderRequestDTO) {
        UUID showTimeId = orderRequestDTO.getShowTimeId();
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
                .status(OrderStatus.PAID)
                .showTime(ShowTime.builder().id(orderRequestDTO.getShowTimeId()).build())
                .build();

        List<OrderDetail> orderDetails = new ArrayList<>();
        float totalPrice = 0;

        for (OrderRequestDTO.Product product : orderRequestDTO.getProducts()) {
            ProductProjection productProjection = productRepository.findWithPriceById(ProductStatus.ACTIVE, false, product.getId());

            float productTotalPrice = productProjection.getPrice() * product.getQuantity();
            totalPrice += productTotalPrice;

            OrderDetail orderDetail = OrderDetail.builder()
                    .price(productProjection.getPrice())
                    .product(Product.builder().id(productProjection.getId()).build())
                    .quantity(product.getQuantity())
                    .type(OrderDetailType.PRODUCT)
                    .order(order)
                    .build();

            orderDetails.add(orderDetail);
        }

        List<Seat> seats = seatRepository.findAllById(orderRequestDTO.getSeatIds());
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
        order = orderRepository.save(order);
        orderDetailRepository.saveAll(orderDetails);

        return new SuccessResponse<>(201, "success", "Tạo đơn hàng thành công", null);
    }

    private String generateOrderCode() {
        long orderCount = orderRepository.count();
        return String.format("HD%08d", (orderCount % 100000000) + 1);
    }
}
