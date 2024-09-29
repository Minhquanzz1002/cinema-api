package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.v1.PromotionProjection;
import vn.edu.iuh.services.PromotionService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/promotions")
@Tag(name = "Promotion Controller", description = "Quản lý khuyến mãi")
public class PromotionController {
    private final PromotionService promotionService;

    @Operation(summary = "Danh sách chương trình khuyến mãi")
    @GetMapping
    public SuccessResponse<List<PromotionProjection>> getPromotions() {
        return promotionService.getPromotions();
    }
}
