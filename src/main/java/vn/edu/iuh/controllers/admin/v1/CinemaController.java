package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateCinemaRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.CinemaService;

import static vn.edu.iuh.constant.RouterConstant.ADMIN_CINEMA_BASE_PATH;

/**
 * TODO
 * API lấy danh sách rạp có phân trang và lọc theo ID và name, trạng thái... (dùng chung ?search)
 * API lấy thông tin chi tiết rạp theo ID
 * API thêm mới rạp
 * API cập nhật thông tin rạp
 * API xóa rạp
 */

@Slf4j
@RequiredArgsConstructor
@RequestMapping(ADMIN_CINEMA_BASE_PATH)
@RestController("cinemaControllerAdminV1")
@Tag(name = "Cinema Controller Admin V1", description = "Quản lý rạp")
public class CinemaController {
    private final CinemaService cinemaService;

    @GetMapping
    public SuccessResponse<Page<?>> getAllCinemas(@RequestParam(required = false) String search,
                                                  @RequestParam(required = false) BaseStatus status,
                                                  @PageableDefault Pageable pageable) {
        // TODO: Implement
        return new SuccessResponse<>(200, "success", "Thành công", null);
    }

    @PostMapping
    public SuccessResponse<Cinema> createCinema(@RequestBody @Valid CreateCinemaRequestDTO body) {
        // TODO: Implement
        return new SuccessResponse<>(200, "success", "Thêm rạp chiếu phim thành công", null);
    }
}
