package vn.edu.iuh.services;

import vn.edu.iuh.dto.req.*;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.models.User;
import vn.edu.iuh.projections.v1.OrderProjection;
import vn.edu.iuh.security.UserPrincipal;

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
}
