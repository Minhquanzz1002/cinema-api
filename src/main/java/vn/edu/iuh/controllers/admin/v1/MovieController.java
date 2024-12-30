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
import vn.edu.iuh.dto.admin.v1.movie.req.CreateMovieRequest;
import vn.edu.iuh.dto.admin.v1.movie.req.UpdateMovieRequest;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieResponseDTO;
import vn.edu.iuh.dto.admin.v1.movie.res.AdminMovieFilterResponse;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.MovieStatus;
import vn.edu.iuh.services.MovieService;

import java.util.List;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Movie.BASE)
@RestController("movieControllerAdminV1")
@Tag(name = "ADMIN V1: Movie Controller", description = "Quản lý phim")
public class MovieController {
    private final MovieService movieService;

    @Operation(summary = AdminSwagger.Movie.CREATE_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<Movie> createMovie(
            @RequestBody @Valid CreateMovieRequest request
    ) {
        return new SuccessResponse<>(
                201,
                "success",
                "Thêm phim thành công",
                movieService.createMovie(request)
        );
    }

    @Operation(summary = AdminSwagger.Movie.GET_FOR_SALE_SUM)
    @GetMapping(AdminPaths.Movie.SALE)
    public SuccessResponse<List<AdminMovieResponseDTO>> getMoviesForSales() {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                movieService.getMoviesForSales()
        );
    }

    @Operation(summary = AdminSwagger.Movie.GET_ALL_SUM)
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

    @Operation(summary = AdminSwagger.Movie.FILTER_SUM)
    @GetMapping(AdminPaths.Movie.FILTER)
    public SuccessResponse<AdminMovieFilterResponse> getMovieFilters() {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                movieService.getMovieFilters()
        );
    }

    @Operation(summary = AdminSwagger.Movie.GET_SUM)
    @GetMapping(AdminPaths.Movie.DETAIL)
    public SuccessResponse<?> getMovieByCode(@PathVariable String code) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                movieService.getMovieByCode(code)
        );
    }

    @Operation(summary = AdminSwagger.Movie.UPDATE_SUM)
    @PutMapping(AdminPaths.Movie.UPDATE)
    public SuccessResponse<Movie> updateMovie(
            @PathVariable int id,
            @RequestBody @Valid UpdateMovieRequest request
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật phim thành công",
                movieService.updateMovie(id, request)
        );
    }

    @Operation(summary = AdminSwagger.Movie.DELETE_SUM)
    @DeleteMapping(AdminPaths.Movie.DELETE)
    public SuccessResponse<?> deleteMovie(@PathVariable int id) {
        movieService.deleteMovie(id);
        return new SuccessResponse<>(
                200,
                "success",
                "Xóa phim thành công",
                null
        );
    }
}
