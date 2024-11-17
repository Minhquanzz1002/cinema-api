package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreateProductRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateProductRequestDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.projections.admin.v1.BaseProductProjection;
import vn.edu.iuh.projections.admin.v1.BaseProductWithPriceProjection;
import vn.edu.iuh.projections.v1.ProductProjection;
import vn.edu.iuh.services.ProductService;

import java.util.List;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Product.BASE)
@RestController("productControllerAdminV1")
@Tag(name = "ADMIN V1: Product Management", description = "Quản lý sản phẩm")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = AdminSwagger.Product.CREATE_SUM)
    @PostMapping
    public SuccessResponse<Product> createProduct(
            @RequestBody @Valid CreateProductRequestDTO createProductRequestDTO
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thêm sản phẩm thành công",
                productService.createProduct(createProductRequestDTO)
        );
    }

    @Operation(summary = AdminSwagger.Product.GET_ALL_ACTIVE_SUM)
    @GetMapping("/all-active")
    public SuccessResponse<List<ProductProjection>> getAllActiveProducts() {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                productService.getAllActiveProducts()
        );
    }

    @Operation(summary = "Danh sách 10 sản phẩm theo mã hoặc tên")
    @GetMapping("/list")
    public SuccessResponse<?> getListProducts(
            @RequestParam(required = false, defaultValue = "") String search
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                productService.getListProducts(search)
        );
    }

    @Operation(summary = AdminSwagger.Product.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<Page<BaseProductWithPriceProjection>> getAllProducts(
            @PageableDefault Pageable pageable,
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(required = false) ProductStatus status
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                productService.getAllProductsWithCurrentPrice(pageable, search, status)
        );
    }

    @Operation(summary = AdminSwagger.Product.GET_SUM)
    @GetMapping(AdminPaths.Product.DETAIL)
    public SuccessResponse<BaseProductWithPriceProjection> getProductByCode(@PathVariable String code) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                productService.getProductWithCurrentPriceByCode(code)
        );
    }

    @Operation(summary = AdminSwagger.Product.UPDATE_SUM)
    @PutMapping(AdminPaths.Product.UPDATE)
    public SuccessResponse<BaseProductProjection> updateProduct(
            @PathVariable String code,
            @RequestBody @Valid UpdateProductRequestDTO updateProductRequestDTO
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật sản phẩm thành công",
                productService.updateProduct(code, updateProductRequestDTO)
        );
    }

    @Operation(summary = AdminSwagger.Product.DELETE_SUM)
    @DeleteMapping(AdminPaths.Product.DELETE)
    public SuccessResponse<Void> deleteProduct(@PathVariable String code) {
        productService.deleteProduct(code);
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                null
        );
    }
}
