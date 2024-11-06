package vn.edu.iuh.projections.admin.v1;

import vn.edu.iuh.models.enums.RefundStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AdminRefundProjection {
    UUID getId();
    String getCode();
    String getReason();
    String getRefundMethod();
    float getAmount();
    RefundStatus getStatus();
    LocalDateTime getRefundDate();
    AdminOrderOverviewProjection getOrder();
}
