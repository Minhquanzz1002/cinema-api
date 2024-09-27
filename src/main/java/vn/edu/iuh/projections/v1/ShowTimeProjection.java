package vn.edu.iuh.projections.v1;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface ShowTimeProjection {
    UUID getId();
    LocalDate getStartDate();
    LocalTime getStartTime();
    LocalTime getEndTime();
    String getCinemaName();
    int getTotalSeat();
    int getBookedSeat();

    RoomProjection getRoom();
}
