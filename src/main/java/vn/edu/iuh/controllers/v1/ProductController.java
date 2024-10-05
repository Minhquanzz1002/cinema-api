package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.v1.ProductProjection;
import vn.edu.iuh.services.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
@Tag(name = "Product Controller", description = "Quản lý sản phẩm")
public class ProductController {
    private final ProductService productService;

    @Operation(
            summary = "Danh sách sản phẩm đang bán",
            description = "Những sản phẩm nào không được set giá thì sẽ không trả về"
    )
    @GetMapping
    public SuccessResponse<List<ProductProjection>> getProducts() {
        return new SuccessResponse<>(200, "success", "Thành công", productService.getProducts());
    }

}
