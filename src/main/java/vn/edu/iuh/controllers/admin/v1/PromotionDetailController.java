package vn.edu.iuh.controllers.admin.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.UpdatePromotionDetailRequestDTO;
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

    @PutMapping("/{id}")
    public SuccessResponse<?> updatePromotionDetail(
            @PathVariable int id,
            @RequestBody @Valid UpdatePromotionDetailRequestDTO updatePromotionDetailRequestDTO) {
        promotionDetailService.updatePromotionDetailById(id, updatePromotionDetailRequestDTO);
        return new SuccessResponse<>(200, "success", "Cập nhật chi tiết chương trình giảm giá thành công", null);
    }
}
