package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.MovieFiltersResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.MovieStatus;
import vn.edu.iuh.projections.admin.v1.*;
import vn.edu.iuh.projections.v1.MovieProjection;
import vn.edu.iuh.repositories.*;
import vn.edu.iuh.services.MovieService;
import vn.edu.iuh.specifications.MovieSpecification;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ProducerRepository producerRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final ModelMapper modelMapper;

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

    @Override
    public MovieFiltersResponseDTO getMovieFilters() {
        List<GenreProjection> genres = genreRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, GenreProjection.class);
        List<ProducerProjection> producers = producerRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, ProducerProjection.class);
        List<ActorProjection> actors = actorRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, ActorProjection.class);
        List<DirectorProjection> directors = directorRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, DirectorProjection.class);
        return MovieFiltersResponseDTO.builder()
                .genres(genres)
                .producers(producers)
                .actors(actors)
                .directors(directors)
                .build();
    }

    @Override
    public Page<AdminMovieResponseDTO> getAllMovies(String search, String country, AgeRating ageRating, MovieStatus status, Pageable pageable) {
        Specification<Movie> spec = Specification.where(null);
        spec = spec.and(MovieSpecification.withAgeRating(ageRating))
                .and(MovieSpecification.withCountry(country))
                .and(MovieSpecification.withStatus(status))
                .and(MovieSpecification.withTitleOrCode(search))
                .and(MovieSpecification.withDeleted(false));
        Page<Movie> movies = movieRepository.findAll(spec, pageable);
        return movies.map(movie -> modelMapper.map(movie, AdminMovieResponseDTO.class));
    }

    @Override
    public Movie getMovieByCode(String code) {
        return movieRepository.findByCodeAndDeleted(code, false).orElseThrow(() -> new DataNotFoundException(""));
    }
}
