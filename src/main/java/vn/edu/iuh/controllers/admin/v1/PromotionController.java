package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.admin.v1.req.CreatePromotionLineRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreatePromotionRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdatePromotionRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminPromotionResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Promotion;
import vn.edu.iuh.models.PromotionLine;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.projections.admin.v1.AdminPromotionLineOverviewProjection;
import vn.edu.iuh.projections.admin.v1.AdminPromotionOverviewProjection;
import vn.edu.iuh.services.PromotionService;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/v1/promotions")
@RestController("promotionControllerAdminV1")
@Tag(name = "Promotion Controller Admin V1", description = "Quản lý khuyến mãi")
public class PromotionController {
    private final PromotionService promotionService;

    @GetMapping
    public SuccessResponse<Page<AdminPromotionResponseDTO>> getAllPromotions(@PageableDefault Pageable pageable,
                                                                             @RequestParam(defaultValue = "", required = false) String search,
                                                                             @RequestParam(required = false) LocalDate startDate,
                                                                             @RequestParam(required = false) LocalDate endDate,
                                                                             @RequestParam(required = false) BaseStatus status) {
        return new SuccessResponse<>(200, "success", "Thành công", promotionService.getAllPromotions(pageable, search, status, startDate, endDate));
    }

    @GetMapping("/{code}")
    public SuccessResponse<AdminPromotionOverviewProjection> getPromotion(@PathVariable String code) {
        return new SuccessResponse<>(200, "success", "Thành công", promotionService.getPromotionByCode(code));
    }

    @Operation(summary = "Tạo chiến dịch khuyến mãi")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<Promotion> createPromotion(@RequestBody @Valid CreatePromotionRequestDTO createPromotionRequestDTO) {
        return new SuccessResponse<>(201, "success", "Thêm chiến dịch khuyến mãi thành công", promotionService.createPromotion(createPromotionRequestDTO));
    }

    @DeleteMapping("/{id}")
    public SuccessResponse<?> deletePromotion(@PathVariable int id) {
        promotionService.deletePromotionById(id);
        return new SuccessResponse<>(200, "success", "Xóa khuyến mãi thành công", null);
    }

    @Operation(summary = "Cập nhật chiến dịch khuyến mãi")
    @PutMapping("/{id}")
    public SuccessResponse<Promotion> updatePromotion(@PathVariable int id, @RequestBody @Valid UpdatePromotionRequestDTO updatePromotionRequestDTO) {
        return new SuccessResponse<>(200, "success", "Cập nhật khuyến mãi thành công", promotionService.updatePromotion(id, updatePromotionRequestDTO));
    }

    @GetMapping("/{id}/lines")
    public SuccessResponse<Page<AdminPromotionLineOverviewProjection>> getPromotionLines(@PathVariable int id, @PageableDefault Pageable pageable) {
        return new SuccessResponse<>(200, "success", "Thành công", promotionService.getPromotionLines(id, pageable));
    }

    @PostMapping("/{id}/lines")
    public SuccessResponse<PromotionLine> createPromotionLine(@PathVariable int id, @RequestBody @Valid CreatePromotionLineRequestDTO createPromotionLineRequestDTO) {
        return new SuccessResponse<>(201, "success", "Thêm chương trình khuyến mãi thành công", promotionService.createPromotionLine(id, createPromotionLineRequestDTO));
    }
}
