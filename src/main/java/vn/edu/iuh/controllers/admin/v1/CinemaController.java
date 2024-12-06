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
import vn.edu.iuh.dto.admin.v1.req.RoomDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateCinemaRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminCinemaDetailResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.CinemaService;
import vn.edu.iuh.services.RoomService;

import java.util.List;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Cinema.BASE)
@RestController("cinemaControllerAdminV1")
@Tag(name = "ADMIN V1: Cinema Management", description = "Quản lý rạp")
public class CinemaController {
    private final CinemaService cinemaService;
    private final RoomService roomService;

    @GetMapping
    @Operation(summary = AdminSwagger.Cinema.GET_ALL_SUM)
    @ApiResponse(responseCode = "200", description = "Success")
    public SuccessResponse<Page<Cinema>> getAllCinemas(
            @Parameter(description = "Tên hoặc code")
            @RequestParam(required = false, defaultValue = "") String search,
            @Parameter(description = "Trạng thái")
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

    @GetMapping(AdminPaths.Cinema.DETAIL)
    @Operation(summary = AdminSwagger.Cinema.GET_SUM)
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
    @Operation(summary = AdminSwagger.Cinema.CREATE_SUM)
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

    @PutMapping(AdminPaths.Cinema.UPDATE)
    @Operation(summary = AdminSwagger.Cinema.UPDATE_SUM)
    @ApiResponse(responseCode = "200", description = "Updated successfully")
    public SuccessResponse<Cinema> updateCinema(
            @Parameter(description = "Rạp ID")
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

    @DeleteMapping(AdminPaths.Cinema.DELETE)
    @Operation(summary = AdminSwagger.Cinema.DELETE_SUM)
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

    @Operation(summary = AdminSwagger.Cinema.GET_ROOMS_SUM)
    @GetMapping(AdminPaths.Cinema.GET_ROOMS)
    public SuccessResponse<List<RoomDTO>> getRoomsByCinemaId(@PathVariable Integer id) {
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Lấy danh sách phòng chiếu thành công",
                roomService.getRoomsByCinemaId(id)
        );
    }
}
