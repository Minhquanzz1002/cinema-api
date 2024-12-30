package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.product.req.CreateProductRequest;
import vn.edu.iuh.dto.admin.v1.product.req.UpdateProductRequest;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.projections.admin.v1.BaseProductProjection;
import vn.edu.iuh.projections.admin.v1.BaseProductWithPriceProjection;
import vn.edu.iuh.projections.v1.ProductProjection;

import java.util.List;

public interface ProductService {
    List<ProductProjection> getAllActiveProducts();

    List<ProductProjection> getProducts();

    Page<BaseProductWithPriceProjection> getAllProductsWithCurrentPrice(Pageable pageable, String search, ProductStatus status);

    BaseProductWithPriceProjection getProductWithCurrentPriceByCode(String code);

    Product createProduct(CreateProductRequest request);

    BaseProductProjection updateProduct(String code, UpdateProductRequest request);

    Product getProductByCode(String code);

    void deleteProduct(String code);

    List<BaseProductProjection> getListProducts(String search);
}
