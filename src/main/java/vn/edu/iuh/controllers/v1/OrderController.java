package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.req.OrderRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.v1.OrderProjection;
import vn.edu.iuh.security.UserPrincipal;
import vn.edu.iuh.services.OrderService;

import java.util.List;

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
    public SuccessResponse<?> createOrder(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid OrderRequestDTO orderRequestDTO) {
        return orderService.createOrder(userPrincipal, orderRequestDTO);
    }

    @Operation(
            summary = "Lịch sử vé đã đặt"
    )
    @GetMapping
    public SuccessResponse<List<OrderProjection>> getOrders(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return orderService.getOrderHistory(userPrincipal);
    }
}
