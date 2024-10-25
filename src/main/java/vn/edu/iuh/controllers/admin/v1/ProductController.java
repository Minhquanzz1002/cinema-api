package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateProductPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateProductRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateProductPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateProductRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.ProductPrice;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.projections.admin.v1.BaseProductProjection;
import vn.edu.iuh.projections.admin.v1.BaseProductWithPriceProjection;
import vn.edu.iuh.services.ProductService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/products")
@RestController("productControllerAdminV1")
@Tag(name = "Product Controller Admin V1", description = "Quản lý sản phẩm")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public SuccessResponse<Page<BaseProductWithPriceProjection>> getAllProducts(@PageableDefault Pageable pageable,
                                                                                @RequestParam(defaultValue = "", required = false) String search,
                                                                                @RequestParam(required = false) ProductStatus status) {
        return new SuccessResponse<>(200, "success", "Thành công", productService.getAllProductsWithCurrentPrice(pageable, search, status));
    }

    @GetMapping("/{code}")
    public SuccessResponse<BaseProductWithPriceProjection> getProductByCode(@PathVariable String code) {
        return new SuccessResponse<>(200, "success", "Thành công", productService.getProductWithCurrentPriceByCode(code));
    }

    @DeleteMapping("/{code}")
    public SuccessResponse<?> deleteProduct(@PathVariable String code) {
        productService.deleteProduct(code);
        return new SuccessResponse<>(200, "success", "Thành công", null);
    }

    @PutMapping("/{code}")
    public SuccessResponse<BaseProductProjection> updateProduct(@PathVariable String code, @RequestBody @Valid UpdateProductRequestDTO updateProductRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", productService.updateProduct(code, updateProductRequestDTO));
    }

    @GetMapping("/{code}/price-histories")
    public SuccessResponse<Page<ProductPrice>> getProductPricesHistory(@PathVariable String code,
                                                                       @PageableDefault @SortDefault.SortDefaults({
                                                                               @SortDefault(sort = "status", direction = Sort.Direction.ASC),
                                                                               @SortDefault(sort = "startDate", direction = Sort.Direction.DESC)
                                                                       }) Pageable pageable,
                                                                       @RequestParam(required = false) BaseStatus status) {
        return new SuccessResponse<>(200, "success", "Thành công", productService.getProductPricesHistory(code, status, pageable));
    }

    @PostMapping("/{code}/prices")
    public SuccessResponse<ProductPrice> createProductPrice(@PathVariable String code, @RequestBody @Valid CreateProductPriceRequestDTO createProductPriceRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", productService.createProductPrice(code, createProductPriceRequestDTO));
    }

    @DeleteMapping("/{code}/prices/{id}")
    public SuccessResponse<?> deleteProductPrice(@PathVariable String code, @PathVariable int id) {
        productService.deleteProductPrice(code, id);
        return new SuccessResponse<>(200, "success", "Thành công", null);
    }

    @PutMapping("/{code}/prices/{id}")
    public SuccessResponse<ProductPrice> updateProductPrice(@PathVariable String code, @PathVariable int id, @RequestBody @Valid UpdateProductPriceRequestDTO updateProductPriceRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", productService.updateProductPrice(code, id, updateProductPriceRequestDTO));
    }

    @PostMapping
    public SuccessResponse<Product> createProduct(@RequestBody @Valid CreateProductRequestDTO createProductRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", productService.createProduct(createProductRequestDTO));
    }
}
