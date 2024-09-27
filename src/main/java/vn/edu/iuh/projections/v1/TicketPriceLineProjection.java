package vn.edu.iuh.projections.v1;

import vn.edu.iuh.models.enums.SeatType;

public interface TicketPriceLineProjection {
    SeatType getSeatType();
    float getPrice();
}
