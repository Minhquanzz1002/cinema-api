package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.ShowTimeFiltersResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.ShowTimeService;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/show-times")
@RestController("showTimeControllerAdminV1")
@Tag(name = "Show Time Controller Admin V1", description = "Quản lý đặt hàng")
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @GetMapping
    public SuccessResponse<AdminShowTimeResponseDTO> getAllShowTimes(@RequestParam int cinemaId,
                                                                     @RequestParam(required = false) LocalDate startDate,
                                                                     @RequestParam(required = false) Integer movieId,
                                                                     @RequestParam(required = false) BaseStatus status) {
        return new SuccessResponse<>(200, "success", "Thành công", showTimeService.getAllShowTimes(cinemaId, startDate, movieId, status));
    }

    @GetMapping("/filters")
    public SuccessResponse<ShowTimeFiltersResponseDTO> getShowTimeFilters() {
        return new SuccessResponse<>(200, "success", "Thành công", showTimeService.getShowTimeFilters());
    }
}
