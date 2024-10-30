package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.Room;
import vn.edu.iuh.models.ShowTime;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.MovieStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, UUID>, JpaSpecificationExecutor<ShowTime> {
    <T> List<T> findAllProjectionBy(Class<T> classType);

    <T> List<T> findAllByMovieAndStartDateAndDeletedAndStatus(Movie movie, LocalDate startDate, boolean deleted, BaseStatus status, Class<T> classType);

    <T> List<T> findAllByMovieAndStartDateAndCinemaAndDeletedAndStatus(Movie movie, LocalDate startDate, Cinema cinema, boolean deleted, BaseStatus status, Class<T> classType);

    <T> Page<T> findAllByStatusAndDeleted(BaseStatus status, boolean deleted, Pageable pageable, Class<T> classType);

    @Query("""
       SELECT CASE WHEN COUNT(st) > 0 THEN true ELSE false END
       FROM ShowTime st
       WHERE st.room = :room
       AND st.startDate = :startDate
       AND (
           (st.startTime <= :endTime AND st.endTime >= :startTime) \s
           OR\s
           (st.startTime >= :startTime AND st.startTime <= :endTime)
       )
       AND st.deleted = false
   """)
    boolean existsByRoomAndStartDateAndStartTimeBetweenOrEndTimeBetween(Room room, LocalDate startDate, LocalTime startTime, LocalTime endTime);

    Optional<ShowTime> findByIdAndDeleted(UUID id, boolean deleted);
}
