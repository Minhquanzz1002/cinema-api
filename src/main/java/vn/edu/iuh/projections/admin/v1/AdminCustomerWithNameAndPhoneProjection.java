package vn.edu.iuh.projections.admin.v1;

import java.util.UUID;

public interface AdminCustomerWithNameAndPhoneProjection {
    UUID getId();
    String getName();
    String getPhone();
}
