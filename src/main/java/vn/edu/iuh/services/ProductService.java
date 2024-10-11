package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.projections.admin.v1.BaseProductProjection;
import vn.edu.iuh.projections.admin.v1.BaseProductWithPriceProjection;
import vn.edu.iuh.projections.v1.ProductProjection;

import java.util.List;

public interface ProductService {
    List<ProductProjection> getProducts();
    Page<BaseProductWithPriceProjection> getAllProductsWithCurrentPrice(Pageable pageable);
}
