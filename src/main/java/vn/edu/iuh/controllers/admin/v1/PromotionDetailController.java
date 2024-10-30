package vn.edu.iuh.controllers.admin.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.PromotionDetailService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/promotion-details")
public class PromotionDetailController {
    private final PromotionDetailService promotionDetailService;

    @DeleteMapping("/{id}")
    public SuccessResponse<?> deletePromotionDetail(@PathVariable int id) {
        promotionDetailService.deletePromotionDetailById(id);
        return new SuccessResponse<>(200, "success", "Xóa chi tiết chương trình giảm giá thành công", null);
    }
}
