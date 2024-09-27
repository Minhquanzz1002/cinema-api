package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.GenreService;

import java.util.List;

@RestController
@RequestMapping("/v1/genres")
@RequiredArgsConstructor
@Tag(name = "Genre Controller", description = "Quản lý danh mục phim")
public class GenreController {
    private final GenreService genreService;

    @Operation(
            summary = "Danh sách danh mục phim"
    )
    @GetMapping
    public SuccessResponse<List<?>> getGenres() {
        return genreService.getGenres();
    }
}
