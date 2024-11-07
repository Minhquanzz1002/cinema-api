package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.constant.SecurityConstant;
import vn.edu.iuh.dto.admin.v1.res.AdminDailyReportResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.ReportService;

import java.time.LocalDate;
import java.util.List;

import static vn.edu.iuh.constant.RouterConstant.*;
import static vn.edu.iuh.constant.SwaggerConstant.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ADMIN_REPORT_BASE_PATH)
@Tag(name = "Report Controller Admin V1", description = "Báo cáo")
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = GET_ADMIN_REPORT_DAILY_SUM)
    @PreAuthorize(SecurityConstant.ROLE_SUPER_ADMIN)
    @GetMapping(GET_ADMIN_REPORT_DAILY_SUB_PATH)
    public SuccessResponse<List<AdminDailyReportResponseDTO>> getDailyReport(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate
    ) {
        return new SuccessResponse<>(200, "success", "Thành công", reportService.getDailyReport(fromDate, toDate));
    }
}
