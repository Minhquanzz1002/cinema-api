package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.enums.MovieStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer>, JpaSpecificationExecutor<Movie> {
    <T> Page<T> findAllByStatusAndDeletedAndTitleContaining(
            Pageable pageable,
            MovieStatus status,
            boolean deleted,
            String title,
            Class<T> classType
    );

    <T> Page<T> findAllByStatusAndDeleted(Pageable pageable, MovieStatus status, boolean deleted, Class<T> classType);

    Optional<Movie> findBySlugAndDeleted(String slug, boolean deleted);

    Optional<Movie> findByCodeAndDeleted(String code, boolean deleted);

    Optional<Movie> findByIdAndDeleted(int id, boolean deleted);

    Optional<Movie> findTopByOrderByCodeDesc();

    <T> List<T> findAllProjectionByDeleted(boolean deleted, Class<T> classType);

    <T> List<T> findAllProjectionByDeletedAndStatus(boolean deleted, MovieStatus status, Class<T> classType);

    List<Movie> findAllByIdInAndDeleted(List<Integer> ids, boolean deleted);

    List<Movie> findAllByStatusAndDeleted(MovieStatus status, boolean deleted);

    boolean existsBySlug(String slug);

    int countAllByStatusAndDeleted(MovieStatus status, boolean deleted);
}
