package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateMovieRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateMovieRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.MovieFiltersResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.MovieStatus;
import vn.edu.iuh.services.MovieService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/movies")
@RestController("movieControllerAdminV1")
@Tag(name = "Movie Controller Admin V1", description = "Quản lý phim")
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/sales")
    public SuccessResponse<List<AdminMovieResponseDTO>> getMoviesForSales() {
        List<AdminMovieResponseDTO> movies = movieService.getMoviesForSales();
        return new SuccessResponse<>(200, "success", "Thành công", movies);
    }

    @GetMapping
    public SuccessResponse<Page<AdminMovieResponseDTO>> getMovies(@PageableDefault(sort = "title") Pageable pageable,
                                                                  @RequestParam(required = false) String search,
                                                                  @RequestParam(required = false) String country,
                                                                  @RequestParam(required = false) AgeRating ageRating,
                                                                  @RequestParam(required = false) MovieStatus status) {
        Page<AdminMovieResponseDTO> moviePage = movieService.getAllMovies(search, country, ageRating, status, pageable);
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

    @DeleteMapping("/{id}")
    public SuccessResponse<?> deleteMovie(@PathVariable int id) {
        movieService.deleteMovie(id);
        return new SuccessResponse<>(200, "success", "Xóa phim thành công", null);
    }

    @Operation(summary = "Cập nhật phim")
    @PutMapping("/{id}")
    public SuccessResponse<Movie> updateMovie(@PathVariable int id, @RequestBody @Valid UpdateMovieRequestDTO updateMovieRequestDTO) {
        return new SuccessResponse<>(200, "success", "Cập nhật phim thành công", movieService.updateMovie(id, updateMovieRequestDTO));
    }

    @Operation(summary = "Thêm phim")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<Movie> createMovie(@RequestBody @Valid CreateMovieRequestDTO createMovieRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thêm phim thành công", movieService.createMovie(createMovieRequestDTO));
    }
}
