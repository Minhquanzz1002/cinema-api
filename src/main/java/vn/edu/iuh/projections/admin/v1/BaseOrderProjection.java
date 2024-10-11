package vn.edu.iuh.projections.admin.v1;

import vn.edu.iuh.models.BaseEntity;
import vn.edu.iuh.models.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BaseOrderProjection extends BaseProjection {
    UUID getId();

    String getCode();

    float getTotalPrice();

    float getFinalAmount();

    float getTotalDiscount();

    LocalDateTime getOrderDate();
    UserInOrderProjection getUser();

    OrderStatus getStatus();
}
