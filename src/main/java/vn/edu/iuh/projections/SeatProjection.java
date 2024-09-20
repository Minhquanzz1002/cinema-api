package vn.edu.iuh.projections;

import vn.edu.iuh.models.enums.SeatStatus;
import vn.edu.iuh.models.enums.SeatType;

public interface SeatProjection {
    int getId();
    short getArea();
    short getColumnIndex();
    short getRowIndex();
    SeatType getType();
    SeatStatus getStatus();
}
