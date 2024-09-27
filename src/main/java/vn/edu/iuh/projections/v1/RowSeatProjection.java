package vn.edu.iuh.projections.v1;

import java.util.List;

public interface RowSeatProjection {
    int getIndex();
    String getName();
    List<SeatProjection> getSeats();
}
