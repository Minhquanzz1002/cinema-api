package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateDirectorRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateDirectorRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Director;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.DirectorService;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Director.BASE)
@RestController("directorControllerAdminV1")
@Tag(name = "ADMIN V1: Director Management", description = "Quản lý đạo diễn")
public class DirectorController {
    private final DirectorService directorService;

    @Operation(summary = AdminSwagger.Director.CREATE_SUM)
    @PostMapping
    public SuccessResponse<Director> createDirector(
            @RequestBody @Valid CreateDirectorRequestDTO request
    ) {
        return new SuccessResponse<>(
                201,
                "success",
                "Thêm đạo diễn thành công",
                directorService.createDirector(request)
        );
    }

    @Operation(summary = AdminSwagger.Director.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<Page<Director>> getDirectors(
            @PageableDefault(sort = "code") Pageable pageable,
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(required = false, defaultValue = "") String country,
            @RequestParam(required = false) BaseStatus status
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                directorService.getAllDirectors(search, status, country, pageable)
        );
    }

    @Operation(summary = AdminSwagger.Director.GET_SUM)
    @GetMapping(AdminPaths.Director.DETAIL)
    public SuccessResponse<Director> getDirector(
            @PathVariable String code
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                directorService.findByCode(code)
        );
    }

    @Operation(summary = AdminSwagger.Director.UPDATE_SUM)
    @PutMapping(AdminPaths.Director.UPDATE)
    public SuccessResponse<Director> updateDirector(
            @PathVariable int id,
            @RequestBody @Valid UpdateDirectorRequestDTO request
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật đạo diễn thành công",
                directorService.updateDirector(id, request)
        );
    }

    @Operation(summary = AdminSwagger.Director.DELETE_SUM)
    @DeleteMapping(AdminPaths.Director.DELETE)
    public SuccessResponse<Void> deleteDirector(
            @PathVariable int id
    ) {
        directorService.deleteDirector(id);
        return new SuccessResponse<>(
                200,
                "success",
                "Xóa đạo diễn thành công",
                null
        );
    }

}
