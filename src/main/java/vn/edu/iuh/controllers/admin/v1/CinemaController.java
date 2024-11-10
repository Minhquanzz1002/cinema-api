package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateCinemaRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateCinemaRequestDTO;
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
    @Operation(summary = "Get all cinemas with pagination and filters")
    @ApiResponse(responseCode = "200", description = "Success")
    public SuccessResponse<Page<Cinema>> getAllCinemas(
            @Parameter(description = "Search by name or code")
            @RequestParam(required = false) String search,
            @Parameter(description = "Filter by status")
            @RequestParam(required = false) BaseStatus status,
            @PageableDefault Pageable pageable) {
        Page<Cinema> cinemas = cinemaService.getAllCinemas(search, status, pageable);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Lấy danh sách rạp thành công",
                cinemas
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cinema details by ID")
    @ApiResponse(responseCode = "200", description = "Success")
    public SuccessResponse<Cinema> getCinemaById(
            @Parameter(description = "Cinema ID")
            @PathVariable Integer id) {
        Cinema cinema = cinemaService.getCinemaById(id);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Lấy thông tin rạp thành công",
                cinema
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new cinema")
    @ApiResponse(responseCode = "201", description = "Created successfully")
    public SuccessResponse<Cinema> createCinema(
            @RequestBody @Valid CreateCinemaRequestDTO body) {
        Cinema cinema = cinemaService.createCinema(body);
        return new SuccessResponse<>(
                HttpStatus.CREATED.value(),
                "success",
                "Thêm rạp chiếu phim thành công",
                cinema
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update cinema information")
    @ApiResponse(responseCode = "200", description = "Updated successfully")
    public SuccessResponse<Cinema> updateCinema(
            @Parameter(description = "Cinema ID")
            @PathVariable Integer id,
            @RequestBody @Valid UpdateCinemaRequestDTO body) {
        Cinema cinema = cinemaService.updateCinema(id, body);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Cập nhật thông tin rạp thành công",
                cinema
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete cinema")
    @ApiResponse(responseCode = "200", description = "Deleted successfully")
    public SuccessResponse<Void> deleteCinema(
            @Parameter(description = "Cinema ID")
            @PathVariable Integer id) {
        cinemaService.deleteCinema(id);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Xóa rạp thành công",
                null
        );
    }
}
