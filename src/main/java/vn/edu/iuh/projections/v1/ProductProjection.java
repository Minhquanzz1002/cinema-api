package vn.edu.iuh.projections.v1;

import vn.edu.iuh.models.enums.ProductStatus;

public interface ProductProjection {
    Integer getId();

    String getName();

    String getDescription();

    String getCode();

    String getImage();

    Float getPrice();

    ProductStatus getStatus();
}
