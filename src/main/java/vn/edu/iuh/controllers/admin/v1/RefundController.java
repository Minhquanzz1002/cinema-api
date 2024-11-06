package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.res.AdminRefundOverviewResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Refund;
import vn.edu.iuh.projections.admin.v1.AdminRefundProjection;
import vn.edu.iuh.services.RefundService;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/v1/refunds")
@Tag(name = "Refund Controller Admin V1", description = "Quản lý hoàn đơn")
public class RefundController {
    private final RefundService refundService;

    @GetMapping
    public SuccessResponse<Page<AdminRefundOverviewResponseDTO>> getAllRefunds(@RequestParam(required = false) String refundCode,
                                                                               @RequestParam(required = false) String orderCode,
                                                                               @PageableDefault Pageable pageable) {
        return new SuccessResponse<>(200, "success", "Thành công", refundService.getAllRefunds(refundCode, orderCode, pageable));
    }

    @GetMapping("/{code}")
    public SuccessResponse<AdminRefundProjection> getRefundByCode(@PathVariable String code) {
        return new SuccessResponse<>(200, "success", "Thành công", refundService.getRefundByCode(code));
    }
}
