package vn.edu.iuh.projections.v1;

import org.springframework.beans.factory.annotation.Value;
import vn.edu.iuh.models.enums.SeatType;

public interface SeatInOrderProjection {
    @Value("#{target.fullName}")
    String getName();
    SeatType getType();
}
