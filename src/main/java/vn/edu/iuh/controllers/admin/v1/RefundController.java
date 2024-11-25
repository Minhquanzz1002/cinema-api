package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.res.AdminRefundOverviewResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.admin.v1.AdminRefundProjection;
import vn.edu.iuh.services.RefundService;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(AdminPaths.Refund.BASE)
@Tag(name = "ADMIN V1: Refund Management", description = "Quản lý hóa đơn hoàn trả")
public class RefundController {
    private final RefundService refundService;

    @Operation(summary = AdminSwagger.Refund.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<Page<AdminRefundOverviewResponseDTO>> getAllRefunds(
            @RequestParam(required = false) String refundCode,
            @RequestParam(required = false) String orderCode,
            @PageableDefault(sort = "refundDate") Pageable pageable
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                refundService.getAllRefunds(refundCode, orderCode, pageable)
        );
    }

    @Operation(summary = AdminSwagger.Refund.GET_SUM)
    @GetMapping(AdminPaths.Refund.DETAIL)
    public SuccessResponse<AdminRefundProjection> getRefundByCode(@PathVariable String code) {
        return new SuccessResponse<>(200, "success", "Thành công", refundService.getRefundByCode(code));
    }
}
