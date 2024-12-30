package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.constant.RouterConstant.ClientPaths;
import vn.edu.iuh.constant.SwaggerConstant.ClientSwagger;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.models.Genre;
import vn.edu.iuh.services.GenreService;

import java.util.List;

@RestController
@RequestMapping(ClientPaths.Genre.BASE)
@RequiredArgsConstructor
@Tag(name = "V1: Genre Controller", description = "Quản lý danh mục phim")
public class GenreController {
    private final GenreService genreService;

    @Operation(summary = ClientSwagger.Genre.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<List<Genre>> getGenres() {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                genreService.getGenres()
        );
    }
}
