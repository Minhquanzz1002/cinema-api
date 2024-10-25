package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.MovieFiltersResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.MovieStatus;
import vn.edu.iuh.projections.admin.v1.AdminMovieProjection;
import vn.edu.iuh.projections.v1.MovieProjection;

public interface MovieService {
    SuccessResponse<Page<MovieProjection>> getMovies(Pageable pageable, String title, MovieStatus status);
    SuccessResponse<Movie> getMovie(String slug);
    MovieFiltersResponseDTO getMovieFilters();
    Page<AdminMovieResponseDTO> getAllMovies(String search, String country, AgeRating ageRating, MovieStatus status, Pageable pageable);
    Movie getMovieByCode(String code);
}
