package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.RefundOrderRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminOrderResponseDTO;
import vn.edu.iuh.dto.req.OrderUpdateProductRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.enums.OrderStatus;
import vn.edu.iuh.projections.admin.v1.AdminOrderOverviewProjection;
import vn.edu.iuh.projections.admin.v1.AdminOrderProjection;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.OrderService;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/orders")
@RestController("orderControllerAdminV1")
@Tag(name = "Order Controller Admin V1", description = "Quản lý đặt hàng")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public SuccessResponse<Page<AdminOrderResponseDTO>> getOrders(@PageableDefault(sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable,
                                                                  @RequestParam(required = false) LocalDate fromDate, @RequestParam(required = false) LocalDate toDate,
                                                                  @RequestParam(required = false) String code, @RequestParam(required = false) OrderStatus status) {
        Page<AdminOrderResponseDTO> orderPage = orderService.getAllOrders(code, status, fromDate, toDate, pageable);
        return new SuccessResponse<>(200, "success", "Thành công", orderPage);
    }

    @GetMapping("/{code}")
    public SuccessResponse<AdminOrderOverviewProjection> getOrderByCode(@PathVariable String code) {
        AdminOrderOverviewProjection order = orderService.getOrderByCode(code);
        return new SuccessResponse<>(200, "success", "Thành công", order);
    }

    @PostMapping
    public SuccessResponse<AdminOrderProjection> createOrderByEmployee(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                       @RequestBody CreateOrderRequestDTO createOrderRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", orderService.createOrderByEmployee(userPrincipal, createOrderRequestDTO));
    }

    @PutMapping("/{orderId}/products")
    public SuccessResponse<AdminOrderProjection> updateProductsInOrderByEmployee(@PathVariable UUID orderId,
                                                                                 @RequestBody OrderUpdateProductRequestDTO orderUpdateProductRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", orderService.updateProductsInOrderByEmployee(orderId, orderUpdateProductRequestDTO));
    }

    @PutMapping("/{orderId}/complete")
    public SuccessResponse<AdminOrderProjection> completeOrder(@PathVariable UUID orderId) {
        return new SuccessResponse<>(200, "success", "Thành công", orderService.completeOrder(orderId));
    }

    @Operation(summary = "Hoàn đơn")
    @PutMapping("/{orderId}/refund")
    public SuccessResponse<Void> refundOrder(@PathVariable UUID orderId, @RequestBody RefundOrderRequestDTO refundOrderRequestDTO) {
        orderService.refundOrder(orderId, refundOrderRequestDTO);
        return new SuccessResponse<>(200, "success", "Hoàn đơn thành công", null);
    }
}
