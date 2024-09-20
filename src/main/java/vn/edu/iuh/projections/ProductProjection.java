package vn.edu.iuh.projections;

import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.models.enums.ProductType;

public interface ProductProjection {
    Integer getId();
    String getName();
    String getDescription();
    String getCode();
    String getImage();
    Float getPrice();
    ProductType getType();
    ProductStatus getStatus();
}
