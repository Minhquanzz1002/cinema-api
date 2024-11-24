package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.RefundOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateCustomerInOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminOrderResponseDTO;
import vn.edu.iuh.dto.req.OrderUpdateProductRequestDTO;
import vn.edu.iuh.dto.req.OrderUpdateSeatRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.enums.OrderStatus;
import vn.edu.iuh.projections.admin.v1.AdminOrderOverviewProjection;
import vn.edu.iuh.projections.admin.v1.AdminOrderProjection;
import vn.edu.iuh.services.OrderService;

import java.time.LocalDate;
import java.util.UUID;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Order.BASE)
@RestController("orderControllerAdminV1")
@Tag(name = "ADMIN V1: Order Management", description = "Quản lý đặt hàng")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = AdminSwagger.Order.CREATE_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<AdminOrderProjection> createOrderByEmployee(
            @RequestBody CreateOrderRequestDTO dto
    ) {
        return new SuccessResponse<>(
                201,
                "success",
                "Thành công",
                orderService.createOrderByEmployee(dto)
        );
    }

    @Operation(summary = AdminSwagger.Order.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<Page<AdminOrderResponseDTO>> getOrders(
            @PageableDefault(sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) OrderStatus status
    ) {
        Page<AdminOrderResponseDTO> orderPage = orderService.getAllOrders(code, status, fromDate, toDate, pageable);
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                orderPage
        );
    }

    @Operation(summary = AdminSwagger.Order.GET_SUM)
    @GetMapping(AdminPaths.Order.DETAIL)
    public SuccessResponse<AdminOrderOverviewProjection> getOrderByCode(@PathVariable String code) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công", orderService.getOrderByCode(code)
        );
    }

    @Operation(summary = AdminSwagger.Order.UPDATE_PRODUCTS_SUM)
    @PutMapping(AdminPaths.Order.UPDATE_PRODUCTS)
    public SuccessResponse<AdminOrderProjection> updateProductsInOrderByEmployee(
            @PathVariable UUID orderId,
            @RequestBody OrderUpdateProductRequestDTO orderUpdateProductRequestDTO
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                orderService.updateProductsInOrderByEmployee(orderId, orderUpdateProductRequestDTO)
        );
    }

    @Operation(summary = AdminSwagger.Order.UPDATE_SEATS_SUM)
    @PutMapping(AdminPaths.Order.UPDATE_SEATS)
    public SuccessResponse<AdminOrderProjection> updateSeatsInOrderByEmployee(
            @PathVariable UUID orderId,
            @RequestBody OrderUpdateSeatRequestDTO dto
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                orderService.updateSeatsInOrderByEmployee(orderId, dto)
        );
    }

    @Operation(summary = AdminSwagger.Order.UPDATE_CUSTOMER_SUM)
    @PutMapping(AdminPaths.Order.UPDATE_CUSTOMER)
    public SuccessResponse<AdminOrderProjection> updateCustomerInOrderByEmployee(
            @PathVariable UUID orderId,
            @RequestBody UpdateCustomerInOrderRequestDTO dto
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                orderService.updateCustomerInOrderByEmployee(orderId, dto)
        );
    }

    @Operation(summary = AdminSwagger.Order.COMPLETE_SUM)
    @PutMapping(AdminPaths.Order.COMPLETE)
    public SuccessResponse<AdminOrderProjection> completeOrder(@PathVariable UUID orderId) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                orderService.completeOrder(orderId)
        );
    }

    @Operation(summary = AdminSwagger.Order.REFUND_SUM)
    @PutMapping(AdminPaths.Order.REFUND)
    public SuccessResponse<Void> refundOrder(
            @PathVariable UUID orderId,
            @RequestBody RefundOrderRequestDTO refundOrderRequestDTO
    ) {
        orderService.refundOrder(orderId, refundOrderRequestDTO);
        return new SuccessResponse<>(
                200,
                "success",
                "Hoàn đơn thành công",
                null
        );
    }

    @Operation(summary = AdminSwagger.Order.CANCEL_SUM)
    @DeleteMapping(AdminPaths.Order.CANCEL)
    public SuccessResponse<Void> cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
        return new SuccessResponse<>(
                200,
                "success",
                "Hủy đơn hàng thành công",
                null
        );
    }
}
