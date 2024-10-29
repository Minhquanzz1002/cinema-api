package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.res.AdminOrderResponseDTO;
import vn.edu.iuh.dto.req.OrderCreateRequestDTO;
import vn.edu.iuh.dto.req.OrderUpdateDiscountDTO;
import vn.edu.iuh.dto.req.OrderUpdateProductRequestDTO;
import vn.edu.iuh.dto.req.OrderUpdateSeatRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.OrderStatus;
import vn.edu.iuh.projections.admin.v1.AdminOrderOverviewProjection;
import vn.edu.iuh.projections.v1.OrderProjection;
import vn.edu.iuh.security.UserPrincipal;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order findByIdAndUser(UUID orderId, User user);
    SuccessResponse<List<OrderProjection>> getOrderHistory(UserPrincipal userPrincipal);
    SuccessResponse<OrderProjection> createOrder(UserPrincipal userPrincipal, OrderCreateRequestDTO orderCreateRequestDTO);
    SuccessResponse<?> cancelOrder(UserPrincipal userPrincipal, UUID orderId);
    SuccessResponse<OrderProjection> completeOrder(UserPrincipal userPrincipal, UUID orderId);
    SuccessResponse<OrderProjection> updateProductsInOrder(UserPrincipal userPrincipal, UUID orderId, OrderUpdateProductRequestDTO orderUpdateProductRequestDTO);
    SuccessResponse<OrderProjection> updateSeatsInOrder(UserPrincipal userPrincipal, UUID orderId, OrderUpdateSeatRequestDTO orderUpdateSeatRequestDTO);
    SuccessResponse<OrderProjection> updateDiscountInOrder(UserPrincipal userPrincipal, UUID orderId, OrderUpdateDiscountDTO orderUpdateDiscountDTO);
    SuccessResponse<OrderProjection> clearDiscountInOrder(UserPrincipal userPrincipal, UUID orderId);
    Page<AdminOrderResponseDTO> getAllOrders(String code, OrderStatus status, LocalDate fromDate, LocalDate toDate, Pageable pageable);
    AdminOrderOverviewProjection getOrderByCode(String code);
}
