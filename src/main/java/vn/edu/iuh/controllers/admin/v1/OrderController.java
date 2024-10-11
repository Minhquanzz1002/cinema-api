package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.admin.v1.AdminOrderOverviewProjection;
import vn.edu.iuh.projections.admin.v1.BaseOrderProjection;
import vn.edu.iuh.projections.v1.OrderProjection;
import vn.edu.iuh.services.OrderService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/orders")
@RestController("orderControllerAdminV1")
@Tag(name = "Order Controller Admin V1", description = "Quản lý đặt hàng")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public SuccessResponse<Page<BaseOrderProjection>> getOrders(@PageableDefault Pageable pageable) {
        Page<BaseOrderProjection> orderPage = orderService.getAllOrders(pageable);
        return new SuccessResponse<>(200, "success", "Thành công", orderPage);
    }

    @GetMapping("/{code}")
    public SuccessResponse<AdminOrderOverviewProjection> getOrderByCode(@PathVariable String code) {
        AdminOrderOverviewProjection order = orderService.getOrderByCode(code);
        return new SuccessResponse<>(200, "success", "Thành công", order);
    }
}
