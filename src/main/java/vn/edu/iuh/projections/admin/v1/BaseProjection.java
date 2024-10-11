package vn.edu.iuh.projections.admin.v1;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BaseProjection {
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    UUID getCreatedBy();
    UUID getUpdatedBy();
}
