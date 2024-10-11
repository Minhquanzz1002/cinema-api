package vn.edu.iuh.projections.admin.v1;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface BaseShowTimeProjection {
    UUID getId();
    LocalTime getStartTime();
    LocalTime getEndTime();
    LocalDate getStartDate();
    int getTotalSeat();
    int getBookedSeat();
}
