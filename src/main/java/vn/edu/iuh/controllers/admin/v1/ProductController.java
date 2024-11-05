package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
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
import vn.edu.iuh.dto.admin.v1.req.CreateProductRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateProductRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.ProductPrice;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.projections.admin.v1.BaseProductProjection;
import vn.edu.iuh.projections.admin.v1.BaseProductWithPriceProjection;
import vn.edu.iuh.projections.v1.ProductProjection;
import vn.edu.iuh.services.ProductService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/products")
@RestController("productControllerAdminV1")
@Tag(name = "Product Controller Admin V1", description = "Quản lý sản phẩm")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all-active")
    public SuccessResponse<List<ProductProjection>> getAllActiveProducts() {
        return new SuccessResponse<>(200, "success", "Thành công", productService.getAllActiveProducts());
    }

    @Operation(summary = "Danh sách 10 sản phẩm theo mã hoặc tên")
    @GetMapping("/list")
    public SuccessResponse<?> getListProducts(@RequestParam(required = false, defaultValue = "") String search) {
        return new SuccessResponse<>(200, "success", "Thành công", productService.getListProducts(search));
    }

    @Operation(summary = "Danh sách sản phẩm (có giá & phân trang)")
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
        return new SuccessResponse<>(200, "success", "Cập nhật sản phẩm thành công", productService.updateProduct(code, updateProductRequestDTO));
    }

    @GetMapping("/{code}/price-histories")
    public SuccessResponse<Page<ProductPrice>> getProductPricesHistory(@PathVariable String code,
                                                                       @RequestParam(required = false) LocalDate startDate,
                                                                       @RequestParam(required = false) LocalDate endDate,
                                                                       @PageableDefault @SortDefault.SortDefaults({
                                                                               @SortDefault(sort = "status", direction = Sort.Direction.ASC),
                                                                               @SortDefault(sort = "startDate", direction = Sort.Direction.DESC)
                                                                       }) Pageable pageable,
                                                                       @RequestParam(required = false) BaseStatus status) {
        return new SuccessResponse<>(200, "success", "Thành công", productService.getProductPricesHistory(code, status, startDate, endDate, pageable));
    }

    @PostMapping
    public SuccessResponse<Product> createProduct(@RequestBody @Valid CreateProductRequestDTO createProductRequestDTO) {
        return new SuccessResponse<>(200, "success", "Thành công", productService.createProduct(createProductRequestDTO));
    }
}
