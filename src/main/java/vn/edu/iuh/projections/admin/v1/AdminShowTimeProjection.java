package vn.edu.iuh.projections.admin.v1;

import org.springframework.beans.factory.annotation.Value;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface AdminShowTimeProjection {
    UUID getId();
    @Value("#{target.movie.title}")
    String getMovieTitle();
    @Value("#{target.room.name}")
    String getRoomName();
    @Value("#{target.cinema.name}")
    String getCinemaName();
    LocalDate getStartDate();
    LocalTime getStartTime();
    LocalTime getEndTime();
    BaseStatus getStatus();
}
