package vn.edu.iuh.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.ShowTimeProjection;
import vn.edu.iuh.services.ShowTimeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/show-times")
@Tag(name = "Show Time Controller", description = "Quản lịch chiếu phim")
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @Operation(
            summary = "Danh sách lịch chiếu"
    )
    @GetMapping
    public SuccessResponse<List<ShowTimeProjection>> getShowTimes(
            @RequestParam int movieId,
            @Parameter(description = "Nếu không truyền thì mặc định sẽ lấy ngày hiện tại (yyy-MM-dd)") @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate date,
            @Parameter(description = "Nếu không truyền thì mặc định sẽ lấy tất cả rạp") @RequestParam(required = false) Integer cinemaId) {

        return showTimeService.getShowTimes(movieId, date, cinemaId);
    }
}
