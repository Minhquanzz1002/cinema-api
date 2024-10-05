package vn.edu.iuh.services;

import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.v1.ProductProjection;

import java.util.List;

public interface ProductService {
    List<ProductProjection> getProducts();
}
