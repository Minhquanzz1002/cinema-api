package vn.edu.iuh.projections.v1;

import vn.edu.iuh.models.enums.OrderStatus;
import vn.edu.iuh.models.enums.RefundStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderProjection {
    UUID getId();

    String getCode();

    float getTotalPrice();
    float getFinalAmount();
    float getTotalDiscount();

    LocalDateTime getOrderDate();

    OrderStatus getStatus();

    ShowTimeInOrderHistoryProjection getShowTime();
    List<OrderDetailProjection> getOrderDetails();

}
