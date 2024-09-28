package vn.edu.iuh.projections.v1;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface ShowTimeProjection {
    UUID getId();
    LocalDate getStartDate();
    LocalTime getStartTime();
    LocalTime getEndTime();
    @Value("#{target.cinema.name}")
    String getCinemaName();
    int getTotalSeat();
    int getBookedSeat();

    RoomProjection getRoom();
}
