package vn.edu.iuh.projections.v1;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BaseEntityProjection {
    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    UUID getCreatedBy();

    UUID getUpdatedBy();
}
