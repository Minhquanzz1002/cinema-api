package vn.edu.iuh.projections;

import java.util.List;

public interface RowSeatProjection {
    int getIndex();
    String getName();
    List<SeatProjection> getSeats();
}
