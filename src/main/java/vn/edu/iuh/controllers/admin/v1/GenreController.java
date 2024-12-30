package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.constant.RouterConstant.AdminPaths;
import vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;
import vn.edu.iuh.dto.admin.v1.genre.req.CreateGenreRequest;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.models.Genre;
import vn.edu.iuh.services.GenreService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Genre.BASE)
@RestController("genreControllerAdminV1")
@Tag(name = "ADMIN V1: Genre Management", description = "Quản lý danh mục phim")
public class GenreController {
    private final GenreService genreService;

    @Operation(summary = AdminSwagger.Genre.CREATE_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<Genre> createGenre(
            @RequestBody @Valid CreateGenreRequest request
    ) {
        return new SuccessResponse<>(
                201,
                "success",
                "Thêm danh mục phim thành công",
                genreService.createGenre(request.getName())
        );
    }
}
