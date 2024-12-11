package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.constant.RouterConstant.AdminPaths;
import vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;
import vn.edu.iuh.dto.admin.v1.res.AdminDashboardStatsResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieRevenueSummaryResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.services.DashboardService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Dashboard.BASE)
@RestController("dashboardControllerAdminV1")
@Tag(name = "ADMIN V1: Dashboard", description = "Dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @Operation(summary = AdminSwagger.Dashboard.GET_STATS)
    @GetMapping(AdminPaths.Dashboard.GET_STATS)
    public SuccessResponse<AdminDashboardStatsResponseDTO> getStats() {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                dashboardService.getStats()
        );
    }

    @Operation(summary = AdminSwagger.Dashboard.GET_MOVIE_REVENUE_SUM)
    @GetMapping(AdminPaths.Dashboard.GET_MOVIE_REVENUE_SUMMARY)
    public SuccessResponse<List<AdminMovieRevenueSummaryResponseDTO>> getMovieRevenueSummary(
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate startDate,
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate endDate
    ) {
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("Ngày bắt đầu không thể sau ngày kết thúc");
        }
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                dashboardService.getMovieRevenueSummary(startDate, endDate)
        );
    }

    @Operation(summary = AdminSwagger.Dashboard.GET_CINEMA_REVENUE_SUM)
    @GetMapping(AdminPaths.Dashboard.GET_CINEMA_REVENUE_SUMMARY)
    public SuccessResponse<?> getCinemaRevenueSummary(
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate startDate,
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate endDate
    ) {

        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                dashboardService.getCinemaRevenueSummary(startDate, endDate)
        );
    }
}
