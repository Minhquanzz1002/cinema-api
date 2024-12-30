package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.showtime.req.BatchActivateShowTimeRequest;
import vn.edu.iuh.dto.admin.v1.showtime.req.CreateShowTimeRequest;
import vn.edu.iuh.dto.admin.v1.showtime.req.GenerateShowTimeRequest;
import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeForSaleResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeResponseDTO;
import vn.edu.iuh.dto.admin.v1.showtime.res.AdminShowTimeFilterResponse;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.ShowTimeService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.ShowTime.BASE)
@RestController("showTimeControllerAdminV1")
@Tag(name = "ADMIN V1: Show Time Controller", description = "Quản lý lịch chiếu")
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @Operation(summary = AdminSwagger.ShowTime.GENERATE_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(AdminPaths.ShowTime.GENERATE)
    public SuccessResponse<Void> generateShowTime(
            @RequestBody @Valid GenerateShowTimeRequest body
    ) {
        return new SuccessResponse<>(
                201,
                "success",
                showTimeService.generateShowTime(body),
                null
        );
    }

    @Operation(summary = AdminSwagger.ShowTime.CREATE_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<Void> createShowTime(@RequestBody @Valid CreateShowTimeRequest request) {
        showTimeService.createShowTime(request);
        return new SuccessResponse<>(
                201,
                "success",
                "Thêm lịch chiếu thành công",
                null
        );
    }

    @Operation(summary = AdminSwagger.ShowTime.GET_FOR_SALE_SUM)
    @GetMapping(AdminPaths.ShowTime.SALE)
    public SuccessResponse<List<AdminShowTimeForSaleResponseDTO>> getShowTimesForSales(
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate startDate,
            @RequestParam Integer movieId,
            @RequestParam(required = false) Integer cinemaId
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                showTimeService.getShowTimesForSales(cinemaId, movieId, startDate)
        );
    }

    @Operation(summary = AdminSwagger.ShowTime.FILTER_SUM)
    @GetMapping(AdminPaths.ShowTime.FILTER)
    public SuccessResponse<AdminShowTimeFilterResponse> getShowTimeFilters() {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                showTimeService.getShowTimeFilters()
        );
    }

    @Operation(summary = AdminSwagger.ShowTime.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<AdminShowTimeResponseDTO> getAllShowTimes(
            @RequestParam int cinemaId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) Integer movieId,
            @RequestParam(required = false) BaseStatus status
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                showTimeService.getAllShowTimes(cinemaId, startDate, movieId, status)
        );
    }

    @Operation(summary = AdminSwagger.ShowTime.UPDATE_SUM)
    @PutMapping(AdminPaths.ShowTime.UPDATE)
    public SuccessResponse<Void> updateShowTime(
            @PathVariable UUID id,
            @RequestBody @Valid CreateShowTimeRequest request
    ) {
        showTimeService.deleteShowTime(id);
        showTimeService.createShowTime(request);
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật lịch chiếu thành công",
                null
        );
    }

    @Operation(summary = AdminSwagger.ShowTime.ACTIVATE_MULTIPLE_SUM)
    @PutMapping(AdminPaths.ShowTime.ACTIVATE_MULTIPLE)
    public SuccessResponse<Void> activateMultipleShowTime(@RequestBody BatchActivateShowTimeRequest body) {
        String resMessage = showTimeService.activateMultipleShowTime(body);
        return new SuccessResponse<>(
                200,
                "success",
                resMessage,
                null
        );
    }

    @Operation(summary = AdminSwagger.ShowTime.DELETE_SUM)
    @DeleteMapping(AdminPaths.ShowTime.DELETE)
    public SuccessResponse<Void> deleteShowTime(@PathVariable UUID id) {
        showTimeService.deleteShowTime(id);
        return new SuccessResponse<>(
                200,
                "success",
                "Xóa lịch chiếu thành công",
                null
        );
    }
}
