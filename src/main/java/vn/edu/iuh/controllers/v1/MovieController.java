package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.enums.MovieStatus;
import vn.edu.iuh.projections.v1.MovieProjection;
import vn.edu.iuh.services.MovieService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/movies")
@Tag(name = "Movie Controller", description = "Quản lý phim")
public class MovieController {
    private final MovieService movieService;

    @Operation(
            summary = "Danh sách phim",
            description = """
                    Chỉ lấy 1 số trường nhất định
                    
                    Status:
                    - ACTIVE: Đang chiếu
                    - COMING_SOON: Sắp chiếu
                    - INACTIVE: Ngưng chiếu hoàn toàn
                    - SUSPENDED: Chỉ tạm ngưng chiếu
                    """
    )
    @GetMapping
    public SuccessResponse<Page<MovieProjection>> getMovies(
            @PageableDefault(sort = "title") Pageable pageable,
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "ACTIVE") MovieStatus status) {
        return movieService.getMovies(pageable, title, status);
    }

    @Operation(
            summary = "Phim theo slug",
            description = "Lấy toàn bộ các trường"
    )
    @GetMapping("/{slug}")
    public SuccessResponse<Movie> getMovie(@PathVariable String slug) {
        return movieService.getMovie(slug);
    }
}
