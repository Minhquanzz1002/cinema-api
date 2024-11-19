package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.constant.SecurityConstant;
import vn.edu.iuh.dto.admin.v1.res.AdminEmployeeReportResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieReportResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminPromotionSummaryResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.services.ReportService;
import vn.edu.iuh.utils.DateRange;
import vn.edu.iuh.utils.DateRangeValidator;

import java.time.LocalDate;
import java.util.List;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(AdminPaths.Report.BASE)
@Tag(name = "ADMIN V1: Report Controller", description = "Báo cáo")
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = AdminSwagger.Report.GET_REPORT_MOVIE_SALE_SUM)
    @GetMapping(AdminPaths.Report.MOVIE_SALE)
    public SuccessResponse<List<AdminMovieReportResponseDTO>> getMovieReport(
            @Parameter(description = "Ngày bắt đầu")
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @Parameter(description = "Tên hoặc ID phim")
            @RequestParam(required = false, defaultValue = "") String search
    ) {
        DateRange dateRange = DateRangeValidator.validateAndGetDateRange(fromDate, toDate);

        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                reportService.getMovieSalesPerformanceReport(dateRange.getFromDate(), dateRange.getToDate(), search)
        );
    }

    @Operation(summary = AdminSwagger.Report.GET_REPORT_EMPLOYEE_SALE_SUM)
    @PreAuthorize(SecurityConstant.HAS_ROLE_SUPER_ADMIN)
    @GetMapping(AdminPaths.Report.EMPLOYEE_SALE)
    public SuccessResponse<List<AdminEmployeeReportResponseDTO>> getEmployeeSaleReport(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @Parameter(description = "Tên hoặc ID nhân viên")
            @RequestParam(required = false, defaultValue = "") String search
    ) {
        DateRange dateRange = DateRangeValidator.validateAndGetDateRange(fromDate, toDate);

        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                reportService.getEmployeeSalesPerformanceReport(dateRange.getFromDate(), dateRange.getToDate(), search)
        );
    }

    @Operation(summary = AdminSwagger.Report.GET_REPORT_PROMOTION_SUM)
    @PreAuthorize(SecurityConstant.HAS_ROLE_SUPER_ADMIN)
    @GetMapping(AdminPaths.Report.PROMOTION)
    public SuccessResponse<List<AdminPromotionSummaryResponseDTO>> getPromotionReport(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) String code
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                reportService.getPromotionReport(fromDate, toDate, code)
        );
    }

}
