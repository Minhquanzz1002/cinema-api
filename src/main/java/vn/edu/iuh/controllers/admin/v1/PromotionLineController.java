package vn.edu.iuh.controllers.admin.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.PromotionLine;
import vn.edu.iuh.services.PromotionLineService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/promotion-lines")
public class PromotionLineController {
    private final PromotionLineService promotionLineService;

    @GetMapping("/{code}")
    public SuccessResponse<PromotionLine> getPromotionLineByCode(@PathVariable String code) {
        return new SuccessResponse<>(200, "success", "Thành công", promotionLineService.getPromotionLineByCode(code));
    }
}
