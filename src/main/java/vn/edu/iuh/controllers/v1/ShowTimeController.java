package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.res.RoomLayoutResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.v1.ShowTimeProjection;
import vn.edu.iuh.services.RoomLayoutService;
import vn.edu.iuh.services.ShowTimeService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/show-times")
@Tag(name = "Show Time Controller", description = "Quản lý lịch chiếu phim")
public class ShowTimeController {
    private final ShowTimeService showTimeService;
    private final RoomLayoutService roomLayoutService;

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

    @Operation(
            summary = "Bố trí ghế dựa trên lịch chiếu"
    )
    @GetMapping("/{showTimeId}/seat-layout")
    public SuccessResponse<RoomLayoutResponseDTO> getRoomLayout(@PathVariable UUID showTimeId) {
        return roomLayoutService.getByShowTimeId(showTimeId);
    }
}
