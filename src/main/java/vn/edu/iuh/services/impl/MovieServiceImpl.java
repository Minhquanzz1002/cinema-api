package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.movie.req.CreateMovieRequest;
import vn.edu.iuh.dto.admin.v1.movie.req.UpdateMovieRequest;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieResponseDTO;
import vn.edu.iuh.dto.admin.v1.movie.res.AdminMovieFilterResponse;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.*;
import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.MovieStatus;
import vn.edu.iuh.projections.admin.v1.ActorProjection;
import vn.edu.iuh.projections.admin.v1.DirectorProjection;
import vn.edu.iuh.projections.admin.v1.GenreProjection;
import vn.edu.iuh.projections.admin.v1.ProducerProjection;
import vn.edu.iuh.projections.v1.MovieProjection;
import vn.edu.iuh.repositories.*;
import vn.edu.iuh.services.MovieService;
import vn.edu.iuh.services.SlugifyService;
import vn.edu.iuh.specifications.MovieSpecification;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ProducerRepository producerRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final SlugifyService slugifyService;
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
    public AdminMovieFilterResponse getMovieFilters() {
        List<GenreProjection> genres = genreRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, GenreProjection.class);
        List<ProducerProjection> producers = producerRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, ProducerProjection.class);
        List<ActorProjection> actors = actorRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, ActorProjection.class);
        List<DirectorProjection> directors = directorRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, DirectorProjection.class);
        return AdminMovieFilterResponse.builder()
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

    @Override
    public Movie getMovieById(int id) {
        return movieRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy phim"));
    }

    @Override
    public void deleteMovie(int id) {
        Movie movie = getMovieById(id);
        if (movie.getStatus() == MovieStatus.ACTIVE) {
            throw new DataNotFoundException("Không thể xóa phim đang chiếu");
        }
        movie.setDeleted(true);
        movieRepository.save(movie);
    }

    @Override
    public Movie createMovie(CreateMovieRequest request) {
        List<Actor> actors = actorRepository.findAllById(request.getActors());
        if (actors.size() != request.getActors().size()) {
            throw new DataNotFoundException("Không tìm thấy diễn viên");
        }

        List<Producer> producers = producerRepository.findAllById(request.getProducers());
        if (producers.size() != request.getProducers().size()) {
            throw new DataNotFoundException("Không tìm thấy nhà sản xuất");
        }

        List<Genre> genres = genreRepository.findAllById(request.getGenres());
        if (genres.size() != request.getGenres().size()) {
            throw new DataNotFoundException("Không tìm thấy thể loại");
        }

        List<Director> directors = directorRepository.findAllById(request.getDirectors());
        if (directors.size() != request.getDirectors().size()) {
            throw new DataNotFoundException("Không tìm thấy đạo diễn");
        }

        Movie movie = modelMapper.map(request, Movie.class);
        String slug = slugifyService.generateSlug(movie.getTitle());
        while (movieRepository.existsBySlug(slug)) {
            slug = slugifyService.generateUniqueSlug(movie.getTitle());
        }
        movie.setSlug(slug);
        movie.setCode(generateNextActorCode());
        movie.setActors(actors);
        movie.setProducers(producers);
        movie.setGenres(genres);
        movie.setDirectors(directors);
        return movieRepository.save(movie);
    }

    @Override
    public Movie updateMovie(int id, UpdateMovieRequest request) {
        List<Actor> actors = actorRepository.findAllById(request.getActors());
        if (actors.size() != request.getActors().size()) {
            throw new DataNotFoundException("Không tìm thấy diễn viên");
        }

        List<Producer> producers = producerRepository.findAllById(request.getProducers());
        if (producers.size() != request.getProducers().size()) {
            throw new DataNotFoundException("Không tìm thấy nhà sản xuất");
        }

        List<Genre> genres = genreRepository.findAllById(request.getGenres());
        if (genres.size() != request.getGenres().size()) {
            throw new DataNotFoundException("Không tìm thấy thể loại");
        }

        List<Director> directors = directorRepository.findAllById(request.getDirectors());
        if (directors.size() != request.getDirectors().size()) {
            throw new DataNotFoundException("Không tìm thấy đạo diễn");
        }

        Movie movie = getMovieById(id);
        modelMapper.map(request, movie);
        movie.setActors(actors);
        movie.setProducers(producers);
        movie.setGenres(genres);
        movie.setDirectors(directors);

        return movieRepository.save(movie);
    }

    @Override
    public List<AdminMovieResponseDTO> getMoviesForSales() {
        Specification<Movie> spec = Specification.where(null);
        spec = spec.and(MovieSpecification.withStatus(MovieStatus.ACTIVE))
                .and(MovieSpecification.withDeleted(false));
        List<Movie> movies = movieRepository.findAll(spec);
        return movies.stream().map(movie -> modelMapper.map(movie, AdminMovieResponseDTO.class)).toList();
    }

    private String generateNextActorCode() {
        Optional<Movie> lastActor = movieRepository.findTopByOrderByCodeDesc();
        int nextNumber = 1;
        if (lastActor.isPresent()) {
            String lastCode = lastActor.get().getCode();
            nextNumber = Integer.parseInt(lastCode.substring(2)) + 1;
        }
        return String.format("MV%06d", nextNumber);
    }
}
