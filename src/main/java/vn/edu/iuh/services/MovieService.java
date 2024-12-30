package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.movie.req.CreateMovieRequest;
import vn.edu.iuh.dto.admin.v1.movie.req.UpdateMovieRequest;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieResponseDTO;
import vn.edu.iuh.dto.admin.v1.movie.res.AdminMovieFilterResponse;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.MovieStatus;
import vn.edu.iuh.projections.v1.MovieProjection;

import java.util.List;

public interface MovieService {
    SuccessResponse<Page<MovieProjection>> getMovies(
            Pageable pageable,
            String title,
            MovieStatus status
    );

    SuccessResponse<Movie> getMovie(String slug);

    AdminMovieFilterResponse getMovieFilters();

    Page<AdminMovieResponseDTO> getAllMovies(
            String search,
            String country,
            AgeRating ageRating,
            MovieStatus status,
            Pageable pageable
    );

    Movie getMovieByCode(String code);

    Movie getMovieById(int id);

    void deleteMovie(int id);

    Movie createMovie(CreateMovieRequest request);

    Movie updateMovie(
            int id,
            UpdateMovieRequest request
    );

    List<AdminMovieResponseDTO> getMoviesForSales();
}
