package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateShowTimeRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.GenerateShowTimeRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeForSaleResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.ShowTimeFiltersResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.ShowTimeService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static vn.edu.iuh.constant.RouterConstant.*;
import static vn.edu.iuh.constant.SwaggerConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(ADMIN_SHOWTIME_BASE_PATH)
@RestController("showTimeControllerAdminV1")
@Tag(name = "ADMIN V1: Show Time Controller", description = "Quản lý lịch chiếu")
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @Operation(summary = GET_SHOW_TIME_FOR_SALE_SUB_PATH_SUM)
    @GetMapping(GET_SHOW_TIME_FOR_SALE_SUB_PATH)
    public SuccessResponse<List<AdminShowTimeForSaleResponseDTO>> getShowTimesForSales(@RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate startDate,
                                                                                       @RequestParam Integer movieId,
                                                                                       @RequestParam(required = false) Integer cinemaId) {
        return new SuccessResponse<>(200, "success", "Thành công", showTimeService.getShowTimesForSales(cinemaId, movieId, startDate));
    }

    @Operation(summary = GET_SHOW_TIME_FOR_FILTER_SUB_PATH_SUM)
    @GetMapping(GET_SHOW_TIME_FOR_FILTER_SUB_PATH)
    public SuccessResponse<ShowTimeFiltersResponseDTO> getShowTimeFilters() {
        return new SuccessResponse<>(200, "success", "Thành công", showTimeService.getShowTimeFilters());
    }

    @Operation(summary = GET_SHOW_TIME_SUM)
    @GetMapping
    public SuccessResponse<AdminShowTimeResponseDTO> getAllShowTimes(@RequestParam int cinemaId,
                                                                     @RequestParam(required = false) LocalDate startDate,
                                                                     @RequestParam(required = false) Integer movieId,
                                                                     @RequestParam(required = false) BaseStatus status) {
        return new SuccessResponse<>(200, "success", "Thành công", showTimeService.getAllShowTimes(cinemaId, startDate, movieId, status));
    }

    @Operation(summary = POST_SHOW_TIME_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<?> createShowTime(@RequestBody @Valid CreateShowTimeRequestDTO createShowTimeRequestDTO) {
        showTimeService.createShowTime(createShowTimeRequestDTO);
        return new SuccessResponse<>(201, "success", "Thêm lịch chiếu thành công", null);
    }

    @Operation(summary = DELETE_SHOW_TIME_SUB_PATH_SUM)
    @DeleteMapping(DELETE_SHOW_TIME_SUB_PATH)
    public SuccessResponse<?> deleteShowTime(@PathVariable UUID id) {
        showTimeService.deleteShowTime(id);
        return new SuccessResponse<>(200, "success", "Xóa lịch chiếu thành công", null);
    }

    @Operation(summary = PUT_SHOW_TIME_SUB_PATH_SUM)
    @PutMapping(PUT_SHOW_TIME_SUB_PATH)
    public SuccessResponse<?> updateShowTime(@PathVariable UUID id, @RequestBody @Valid CreateShowTimeRequestDTO createShowTimeRequestDTO) {
        showTimeService.deleteShowTime(id);
        showTimeService.createShowTime(createShowTimeRequestDTO);
        return new SuccessResponse<>(200, "success", "Cập nhật lịch chiếu thành công", null);
    }

    @Operation(summary = POST_SHOW_TIME_GENERATE_SUM)
    @PostMapping(POST_SHOW_TIME_GENERATE_SUB_PATH)
    public SuccessResponse<?> generateShowTime(
            @RequestBody @Valid GenerateShowTimeRequestDTO body
            ) {
        showTimeService.generateShowTime(body);
        return new SuccessResponse<>(
                200,
                "success",
                "Tạo lịch chiếu thành công",
                null
        );
    }
}
