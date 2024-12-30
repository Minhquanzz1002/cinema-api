package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.order.req.RefundOrderRequest;
import vn.edu.iuh.dto.admin.v1.req.UpdateCustomerInOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminOrderResponseDTO;
import vn.edu.iuh.dto.client.v1.order.req.CreateOrderRequest;
import vn.edu.iuh.dto.client.v1.order.req.UpdateOrderDiscountRequest;
import vn.edu.iuh.dto.client.v1.order.req.UpdateOrderProductRequest;
import vn.edu.iuh.dto.client.v1.order.req.UpdateOrderSeatRequest;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.models.Order;
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
            CreateOrderRequest dto
    );

    /**
     * Creates a new order by an employee
     *
     * @param dto The order information
     * @return Containing the created order information
     */
    AdminOrderProjection createOrderByEmployee(vn.edu.iuh.dto.admin.v1.order.req.CreateOrderRequest dto);

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

    /**
     * Updates customer information in an order. Admin only
     *
     * @param orderId The order ID
     * @param dto     The updated customer information
     * @return Updated order information with administrative details
     * @throws vn.edu.iuh.exceptions.DataNotFoundException If the order is not found
     */
    AdminOrderProjection updateOrderCustomer(UUID orderId, UpdateCustomerInOrderRequestDTO dto);

    /**
     * Updates product information in an order for customers.
     *
     * @param principal The authenticated user's information
     * @param orderId   The order ID
     * @param request   The product update details
     * @return Updated order information
     */
    OrderProjection updateOrderProductsByCustomer(
            UserPrincipal principal,
            UUID orderId,
            UpdateOrderProductRequest request
    );

    /**
     * Updates product information in an order for employees.
     *
     * @param orderId The order ID
     * @param request The product update details
     * @return Updated order information
     */
    AdminOrderProjection updateOrderProductsByEmployee(
            UUID orderId,
            UpdateOrderProductRequest request
    );


    /**
     * Updates seat assignments in an order for customers.
     *
     * @param principal The authenticated user's information
     * @param orderId   The order ID
     * @param request   The seat update details
     * @return Updated order information
     */
    OrderProjection updateOrderSeatsByCustomer(
            UserPrincipal principal,
            UUID orderId,
            UpdateOrderSeatRequest request
    );

    /**
     * Updates seat assignments in an order for employee.
     *
     * @param orderId The order ID
     * @param request The seat update details
     * @return Updated order information
     */
    AdminOrderProjection updateOrderSeatsByEmployee(
            UUID orderId,
            UpdateOrderSeatRequest request
    );

    SuccessResponse<OrderProjection> updateDiscountInOrder(
            UserPrincipal principal,
            UUID orderId,
            UpdateOrderDiscountRequest request
    );

    SuccessResponse<OrderProjection> clearDiscountInOrder(UserPrincipal userPrincipal, UUID orderId);

    /**
     * Retrieves an order by code
     *
     * @param code The order code
     * @return The order matching the code
     */
    AdminOrderOverviewProjection getOrderByCode(String code);

    /**
     * Processes a refund for an order. Admin only
     *
     * @param orderId The order ID
     * @param request The refund details
     */
    void refundOrder(UUID orderId, RefundOrderRequest request);

    /**
     * Customer method to cancel an order
     *
     * @param userPrincipal The authenticated user's information
     * @param orderId       The order ID
     */
    void cancelOrderByCustomer(UserPrincipal userPrincipal, UUID orderId);

    /**
     * Administrator method to cancel an order
     *
     * @param orderId The order ID
     */
    void cancelOrderByEmployee(UUID orderId);

    /**
     * Customer method to completes an order
     *
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
