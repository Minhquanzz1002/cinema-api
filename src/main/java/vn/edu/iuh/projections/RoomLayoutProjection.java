package vn.edu.iuh.projections;

import java.util.List;

public interface RoomLayoutProjection {
    int getId();
    int getMaxColumn();
    int getMaxRow();
    List<RowSeatProjection> getRows();
}
