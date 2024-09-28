package vn.edu.iuh.services;

import vn.edu.iuh.dto.req.OrderRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.v1.OrderProjection;
import vn.edu.iuh.security.UserPrincipal;

import java.util.List;

public interface OrderService {
    SuccessResponse<List<OrderProjection>> getOrderHistory(UserPrincipal userPrincipal);
    SuccessResponse<?> createOrder(UserPrincipal userPrincipal, OrderRequestDTO orderRequestDTO);
}
