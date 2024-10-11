package vn.edu.iuh.projections.admin.v1;

import vn.edu.iuh.models.enums.ProductStatus;

public interface BaseProductProjection {
    int getId();
    String getCode();
    String getName();
    String getDescription();
    String getImage();
    ProductStatus getStatus();
}
