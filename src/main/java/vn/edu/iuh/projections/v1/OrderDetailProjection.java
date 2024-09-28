package vn.edu.iuh.projections.v1;

import vn.edu.iuh.models.enums.OrderDetailType;

public interface OrderDetailProjection {
    int getQuantity();
    float getPrice();
    OrderDetailType getType();
    ProductInOrderProjection getProduct();
    SeatInOrderProjection getSeat();
}
