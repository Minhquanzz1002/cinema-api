package vn.edu.iuh.projections.v1;

import vn.edu.iuh.models.enums.SeatStatus;
import vn.edu.iuh.models.enums.SeatType;

import java.util.List;

public interface SeatProjection {
    int getId();
    short getArea();
    short getColumnIndex();
    short getRowIndex();
    SeatType getType();
    SeatStatus getStatus();
    String getName();
    String getFullName();
    List<GroupSeatProjection> getGroupSeats();
}
