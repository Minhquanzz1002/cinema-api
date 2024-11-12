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

import static vn.edu.iuh.constant.RouterConstant.*;
import static vn.edu.iuh.constant.SwaggerConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(ADMIN_MOVIE_BASE_PATH)
@RestController("movieControllerAdminV1")
@Tag(name = "ADMIN V1: Movie Controller", description = "Quản lý phim")
public class MovieController {
    private final MovieService movieService;

    @Operation(summary = GET_ADMIN_MOVIE_FOR_SALE_SUM)
    @GetMapping(GET_ADMIN_MOVIE_FOR_SALE_SUB_PATH)
    public SuccessResponse<List<AdminMovieResponseDTO>> getMoviesForSales() {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                movieService.getMoviesForSales()
        );
    }

    @Operation(summary = GET_ALL_ADMIN_MOVIE_SUM)
    @GetMapping
    public SuccessResponse<Page<AdminMovieResponseDTO>> getMovies(
            @PageableDefault(sort = "title") Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) AgeRating ageRating,
            @RequestParam(required = false) MovieStatus status) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                movieService.getAllMovies(search, country, ageRating, status, pageable)
        );
    }

    @Operation(summary = GET_ADMIN_MOVIE_FOR_FILTER_SUM)
    @GetMapping(GET_ADMIN_MOVIE_FOR_FILTER_SUB_PATH)
    public SuccessResponse<MovieFiltersResponseDTO> getMovieFilters() {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                movieService.getMovieFilters()
        );
    }

    @Operation(summary = GET_ADMIN_MOVIE_SUM)
    @GetMapping(GET_ADMIN_MOVIE_SUB_PATH)
    public SuccessResponse<?> getMovieByCode(@PathVariable String code) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                movieService.getMovieByCode(code)
        );
    }

    @Operation(summary = DELETE_ADMIN_MOVIE_SUM)
    @DeleteMapping(DELETE_ADMIN_MOVIE_SUB_PATH)
    public SuccessResponse<?> deleteMovie(@PathVariable int id) {
        movieService.deleteMovie(id);
        return new SuccessResponse<>(
                200,
                "success",
                "Xóa phim thành công",
                null
        );
    }

    @Operation(summary = PUT_ADMIN_MOVIE_SUM)
    @PutMapping(PUT_ADMIN_MOVIE_SUB_PATH)
    public SuccessResponse<Movie> updateMovie(
            @PathVariable int id,
            @RequestBody @Valid UpdateMovieRequestDTO updateMovieRequestDTO
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật phim thành công",
                movieService.updateMovie(id, updateMovieRequestDTO)
        );
    }

    @Operation(summary = POST_ADMIN_MOVIE_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<Movie> createMovie(@RequestBody @Valid CreateMovieRequestDTO createMovieRequestDTO) {
        return new SuccessResponse<>(
                201,
                "success",
                "Thêm phim thành công",
                movieService.createMovie(createMovieRequestDTO)
        );
    }
}
