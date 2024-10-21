package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.res.MovieFiltersResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.enums.MovieStatus;
import vn.edu.iuh.projections.admin.v1.AdminMovieProjection;
import vn.edu.iuh.projections.v1.MovieProjection;
import vn.edu.iuh.services.MovieService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/movies")
@RestController("movieControllerAdminV1")
@Tag(name = "Movie Controller Admin V1", description = "Quản lý phim")
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public SuccessResponse<Page<AdminMovieProjection>> getMovies(@PageableDefault(sort = "title") Pageable pageable,
                                                                 @RequestParam(required = false) String title,
                                                                 @RequestParam(required = false, defaultValue = "ACTIVE") MovieStatus status) {
        Page<AdminMovieProjection> moviePage = movieService.getAllMovies(pageable, title, status);
        return new SuccessResponse<>(200, "success", "Thành công", moviePage);
    }

    @GetMapping("/filters")
    public SuccessResponse<MovieFiltersResponseDTO> getMovieFilters() {
        MovieFiltersResponseDTO response = movieService.getMovieFilters();
        return new SuccessResponse<>(200, "success", "Thành công", response);
    }

    @GetMapping("/{code}")
    public SuccessResponse<?> getMovieByCode(@PathVariable String code) {
        Movie movie = movieService.getMovieByCode(code);
        return new SuccessResponse<>(200, "success", "Thành công", movie);
    }
}
