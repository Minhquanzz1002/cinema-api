package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.ShowTime;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, UUID> {
    <T> List<T> findAllProjectionBy(Class<T> classType);

    <T> List<T> findAllByMovieAndStartDate(Movie movie, LocalDate startDate, Class<T> classType);

    <T> List<T> findAllByMovieAndStartDateAndCinema(Movie movie, LocalDate startDate, Cinema cinema, Class<T> classType);

    <T> Page<T> findAllByStatusAndDeleted(BaseStatus status, boolean deleted, Pageable pageable, Class<T> classType);
}
