package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.req.OrderCreateRequestDTO;
import vn.edu.iuh.dto.req.OrderUpdateProductRequestDTO;
import vn.edu.iuh.dto.req.OrderUpdateSeatRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.v1.OrderProjection;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.OrderService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
@Tag(name = "Order Controller", description = "Quản lý đặt vé")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "Tạo đơn hàng"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<OrderProjection> createOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid OrderCreateRequestDTO orderCreateRequestDTO) {
        return orderService.createOrder(userPrincipal, orderCreateRequestDTO);
    }

    @Operation(
            summary = "Cập nhật vé của đơn hàng"
    )
    @PutMapping("/{orderId}/seats")
    public SuccessResponse<OrderProjection> updateSeatInOrderDetail(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable UUID orderId, @RequestBody OrderUpdateSeatRequestDTO orderUpdateSeatRequestDTO) {
        return orderService.updateSeatsInOrder(userPrincipal, orderId, orderUpdateSeatRequestDTO);
    }

    @Operation(
            summary = "Cập nhật sản phẩm của đơn hàng"
    )
    @PutMapping("/{orderId}/products")
    public SuccessResponse<OrderProjection> updateProductInOrderDetail(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                         @PathVariable UUID orderId,
                                                         @RequestBody @Valid OrderUpdateProductRequestDTO orderUpdateProductRequestDTO) {
        return orderService.updateProductsInOrder(userPrincipal, orderId, orderUpdateProductRequestDTO);
    }

    @Operation(
            summary = "Xóa đơn hàng",
            description = """
                    Dùng khi người dùng rời khỏi trang đặt vé
                    """
    )
    @DeleteMapping("/{orderId}")
    public SuccessResponse<?> cancelOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable UUID orderId) {
        return orderService.cancelOrder(userPrincipal, orderId);
    }

    @Operation(
            summary = "Xác nhận hoàn tất đơn hàng",
            description = """
                    Dùng khi người dùng xác nhận đặt hàng
                    """
    )
    @PutMapping("/{orderId}/complete")
    public SuccessResponse<?> completeOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable UUID orderId) {
        return orderService.completeOrder(userPrincipal, orderId);
    }

    @Operation(
            summary = "Lịch sử vé đã đặt"
    )
    @GetMapping
    public SuccessResponse<List<OrderProjection>> getOrders(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return orderService.getOrderHistory(userPrincipal);
    }
}
