package vn.edu.iuh.projections.v1;

import vn.edu.iuh.models.enums.ProductStatus;

public interface ProductInOrderProjection {
    Integer getId();

    String getName();

    String getDescription();

    String getCode();

    String getImage();

    ProductStatus getStatus();
}
