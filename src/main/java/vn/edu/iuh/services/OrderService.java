package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.req.CreateOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.RefundOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateCustomerInOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminOrderResponseDTO;
import vn.edu.iuh.dto.req.*;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.OrderStatus;
import vn.edu.iuh.projections.admin.v1.AdminOrderOverviewProjection;
import vn.edu.iuh.projections.admin.v1.AdminOrderProjection;
import vn.edu.iuh.projections.v1.OrderProjection;
import vn.edu.iuh.security.UserPrincipal;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    /**
     * Creates a new order
     *
     * @param principal The user information who creates the order
     * @param dto       The order information
     * @return Containing the created order information
     */
    OrderProjection createOrder(
            UserPrincipal principal,
            OrderCreateRequestDTO dto
    );

    /**
     * Creates a new order by an employee
     *
     * @param dto The order information
     * @return Containing the created order information
     */
    AdminOrderProjection createOrderByEmployee(CreateOrderRequestDTO dto);

    /**
     * Retrieves a paginated list of orders with filtering conditions
     *
     * @param code     Order code to search for (optional)
     * @param status   Order status to search for (optional)
     * @param fromDate Start date for filtering orders (optional)
     * @param toDate   End date for filtering orders (optional)
     * @param pageable Pagination information (page number, size, sort)
     * @return Page containing AdminOrderResponseDTO with filtered and paginated orders
     */
    Page<AdminOrderResponseDTO> getAllOrders(
            String code,
            OrderStatus status,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    );

    /**
     * Retrieves an order by ID and user ID
     *
     * @param orderId The order ID
     * @param userId  The user ID
     * @return The order matching both IDs
     */
    Order findByIdAndUser(UUID orderId, UUID userId);

    /**
     * Retrieves an order by ID
     *
     * @param orderId The order ID
     * @return The order matching the ID
     */
    Order findById(UUID orderId);

    SuccessResponse<List<OrderProjection>> getOrderHistory(UserPrincipal userPrincipal);

    SuccessResponse<?> cancelOrder(UserPrincipal userPrincipal, UUID orderId);

    void cancelOrder(UUID orderId);


    SuccessResponse<OrderProjection> updateProductsInOrder(
            UserPrincipal userPrincipal,
            UUID orderId,
            OrderUpdateProductRequestDTO orderUpdateProductRequestDTO
    );

    AdminOrderProjection updateProductsInOrderByEmployee(
            UUID orderId,
            OrderUpdateProductRequestDTO orderUpdateProductRequestDTO
    );

    AdminOrderProjection updateCustomerInOrderByEmployee(UUID orderId, UpdateCustomerInOrderRequestDTO dto);

    SuccessResponse<OrderProjection> updateSeatsInOrder(
            UserPrincipal userPrincipal,
            UUID orderId,
            OrderUpdateSeatRequestDTO orderUpdateSeatRequestDTO
    );

    AdminOrderProjection updateSeatsInOrderByEmployee(
            UUID orderId,
            OrderUpdateSeatRequestDTO orderUpdateSeatRequestDTO
    );

    SuccessResponse<OrderProjection> updateDiscountInOrder(
            UserPrincipal userPrincipal,
            UUID orderId,
            OrderUpdateDiscountDTO orderUpdateDiscountDTO
    );

    SuccessResponse<OrderProjection> clearDiscountInOrder(UserPrincipal userPrincipal, UUID orderId);

    AdminOrderOverviewProjection getOrderByCode(String code);

    void refundOrder(UUID orderId, RefundOrderRequestDTO refundOrderRequestDTO);

    /**
     * @param principal The authenticated user's information
     * @param orderId   The order ID
     * @return OrderProjection with completed order information
     */
    OrderProjection completeOrder(UserPrincipal principal, UUID orderId);

    /**
     * Administrator method to completes an order
     *
     * @param orderId The order ID
     * @return AdminOrderProjection with completed order information
     */
    AdminOrderProjection completeOrder(UUID orderId);
}
