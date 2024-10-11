package vn.edu.iuh.projections.admin.v1;

import java.util.UUID;

public interface UserInOrderProjection {
    UUID getId();

    String getName();

    String getEmail();

    String getPhone();
}
