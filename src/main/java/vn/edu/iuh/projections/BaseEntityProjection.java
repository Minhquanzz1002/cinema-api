package vn.edu.iuh.projections;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BaseEntityProjection {
    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    UUID getCreatedBy();

    UUID getUpdatedBy();
}
