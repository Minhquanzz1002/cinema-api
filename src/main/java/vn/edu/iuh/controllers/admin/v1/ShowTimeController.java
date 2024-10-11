package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.ShowTime;
import vn.edu.iuh.projections.admin.v1.AdminShowTimeProjection;
import vn.edu.iuh.services.ShowTimeService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/show-times")
@RestController("showTimeControllerAdminV1")
@Tag(name = "Show Time Controller Admin V1", description = "Quản lý đặt hàng")
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @GetMapping
    public SuccessResponse<Page<AdminShowTimeProjection>> getAllShowTimes(@PageableDefault Pageable pageable) {
        return new SuccessResponse<>(200, "success", "Thành công", showTimeService.getAllShowTimes(pageable));
    }
}
