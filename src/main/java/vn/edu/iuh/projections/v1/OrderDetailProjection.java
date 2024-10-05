package vn.edu.iuh.projections.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.edu.iuh.models.enums.OrderDetailType;

public interface OrderDetailProjection {
    Integer getQuantity();
    float getPrice();
    @JsonProperty("isGift")
    boolean isGift();
    OrderDetailType getType();
    ProductInOrderProjection getProduct();
    SeatInOrderProjection getSeat();
}
