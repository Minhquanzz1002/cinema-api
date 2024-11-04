package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.admin.v1.res.AdminProductPriceOverviewResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.services.ProductPriceService;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/product-prices")
@Tag(name = "Product Price Controller Admin V1", description = "Quản lý giá sản phẩm")
public class ProductPriceController {
    private final ProductPriceService productPriceService;


    @Operation(
            summary = "Dánh sách giá sản phẩm",
            description = """
                    startDate: Ngày bắt đầu
                    endDate: Ngày kết thúc
                    status: Trạng thái
                    search: Tìm kiếm theo tên hoặc mã sản phẩm
                    """
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public SuccessResponse<Page<AdminProductPriceOverviewResponseDTO>> getProductPrices(@RequestParam(required = false) LocalDate startDate,
                                                                                        @RequestParam(required = false) LocalDate endDate,
                                                                                        @RequestParam(required = false) BaseStatus status,
                                                                                        @RequestParam(required = false) String search,
                                                                                        @PageableDefault(sort = "startDate") Pageable pageable) {
        return new SuccessResponse<>(200, "success", "Thành công", productPriceService.getAllProductPrices(startDate, endDate, status, search, pageable));
    }
}
