package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.enums.MovieStatus;
import vn.edu.iuh.projections.v1.MovieProjection;
import vn.edu.iuh.repositories.MovieRepository;
import vn.edu.iuh.services.MovieService;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Override
    public SuccessResponse<Page<MovieProjection>> getMovies(Pageable pageable, String title, MovieStatus status) {
        Page<MovieProjection> movies;
        if (title == null || title.trim().isEmpty()) {
            movies = movieRepository.findAllByStatusAndDeleted(pageable, status, false, MovieProjection.class);
        } else {
            movies = movieRepository.findAllByStatusAndDeletedAndTitleContaining(pageable, status, false, title, MovieProjection.class);
        }
        return new SuccessResponse<>(200, "success", "Thành công", movies);
    }

    @Override
    public SuccessResponse<Movie> getMovie(String slug) {
        Movie movie = movieRepository.findBySlugAndDeleted(slug, false).orElseThrow(() -> new DataNotFoundException(""));
        return new SuccessResponse<>(200, "success", "Thành công", movie);
    }
}
