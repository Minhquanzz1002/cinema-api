package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.req.CreateProductPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateProductRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateProductPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateProductRequestDTO;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.ProductPrice;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.projections.admin.v1.BaseProductProjection;
import vn.edu.iuh.projections.admin.v1.BaseProductWithPriceProjection;
import vn.edu.iuh.projections.v1.ProductProjection;

import java.util.List;

public interface ProductService {
    List<ProductProjection> getProducts();
    Page<BaseProductWithPriceProjection> getAllProductsWithCurrentPrice(Pageable pageable, String code, String name, ProductStatus status);
    BaseProductWithPriceProjection getProductWithCurrentPriceByCode(String code);
    Page<ProductPrice> getProductPricesHistory(String code, Pageable pageable);

    Product createProduct(CreateProductRequestDTO createProductRequestDTO);
    BaseProductProjection updateProduct(String code, UpdateProductRequestDTO updateProductRequestDTO);
    Product getProductByCode(String code);
    void deleteProduct(String code);

    ProductPrice createProductPrice(String code, CreateProductPriceRequestDTO createProductPriceRequestDTO);
    void deleteProductPrice(String code, int id);
    ProductPrice updateProductPrice(String code, int id, UpdateProductPriceRequestDTO updateProductPriceRequestDTO);
}
