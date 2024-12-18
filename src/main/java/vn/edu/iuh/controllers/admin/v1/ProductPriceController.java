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
import vn.edu.iuh.dto.admin.v1.req.CreateProductPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateProductPriceRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminProductPriceOverviewResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.ProductPrice;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.ProductPriceService;

import java.time.LocalDate;

import static vn.edu.iuh.constant.RouterConstant.AdminPaths;
import static vn.edu.iuh.constant.SwaggerConstant.AdminSwagger;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(AdminPaths.ProductPrice.BASE)
@Tag(name = "ADMIN V1: Product Price Management", description = "Quản lý giá sản phẩm")
public class ProductPriceController {
    private final ProductPriceService productPriceService;

    @Operation(summary = AdminSwagger.ProductPrice.CREATE_SUM)
    @PostMapping
    public SuccessResponse<Void> createProductPrice(
            @RequestBody @Valid CreateProductPriceRequestDTO createProductPriceRequestDTO
    ) {
        productPriceService.createProductPrice(createProductPriceRequestDTO);
        return new SuccessResponse<>(
                200,
                "success",
                "Thêm bảng giá thành công",
                null
        );
    }

    @Operation(
            summary = AdminSwagger.ProductPrice.GET_ALL_SUM,
            description = """
                    startDate: Ngày bắt đầu
                    endDate: Ngày kết thúc
                    status: Trạng thái
                    search: Tìm kiếm theo tên hoặc mã sản phẩm
                    """
    )
    @GetMapping
    public SuccessResponse<Page<AdminProductPriceOverviewResponseDTO>> getProductPrices(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) BaseStatus status,
            @RequestParam(required = false) String search,
            @PageableDefault(sort = "startDate") Pageable pageable
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                productPriceService.getAllProductPrices(startDate, endDate, status, search, pageable)
        );
    }

    @Operation(
            summary = AdminSwagger.ProductPrice.UPDATE_SUM,
            description = """
                    Check trùng ngày khi trạng thái mới là `ACTIVE`
                    """
    )
    @PutMapping(AdminPaths.ProductPrice.UPDATE)
    public SuccessResponse<ProductPrice> updateProductPrice(
            @PathVariable int id,
            @RequestBody @Valid UpdateProductPriceRequestDTO updateProductPriceRequestDTO
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật giá bảng giá thành công",
                productPriceService.updateProductPrice(id, updateProductPriceRequestDTO)
        );
    }

    @Operation(
            summary = AdminSwagger.ProductPrice.DELETE_SUM,
            description = """
                    Xóa bảng giá sản phẩm theo id
                    
                    Không thể xóa bảng giá đang hoạt động
                    """
    )
    @DeleteMapping(AdminPaths.ProductPrice.DELETE)
    public SuccessResponse<Void> deleteProductPrice(@PathVariable int id) {
        productPriceService.deleteProductPrice(id);
        return new SuccessResponse<>(
                200,
                "success",
                "Xóa giá bảng giá thành công",
                null
        );
    }
}
