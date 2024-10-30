package vn.edu.iuh.controllers.admin.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreatePromotionDetailRequestDTO;
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

    @DeleteMapping("/{id}")
    public SuccessResponse<?> deletePromotionLine(@PathVariable int id) {
        promotionLineService.deletePromotionLineById(id);
        return new SuccessResponse<>(200, "success", "Xóa khuyến mãi thành công", null);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{promotionLineId}/details")
    public SuccessResponse<?> createPromotionLineDetails(@PathVariable int promotionLineId, @RequestBody @Valid CreatePromotionDetailRequestDTO createPromotionDetailRequestDTO) {
        promotionLineService.createPromotionDetail(promotionLineId, createPromotionDetailRequestDTO);
        return new SuccessResponse<>(200, "success", "Thành công", null);
    }
}
