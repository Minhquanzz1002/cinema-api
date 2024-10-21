package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.PromotionService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/promotions")
@RestController("promotionControllerAdminV1")
@Tag(name = "Promotion Controller Admin V1", description = "Quản lý khuyến mãi")
public class PromotionController {
    private final PromotionService promotionService;

    @GetMapping
    public SuccessResponse<Page<?>> getAllPromotions(@PageableDefault Pageable pageable,
                                                     @RequestParam(defaultValue = "") String code,
                                                     @RequestParam(defaultValue = "") String name) {
        return new SuccessResponse<>(200, "success", "Thành công", promotionService.getAllPromotions(pageable, code, name));
    }
}
