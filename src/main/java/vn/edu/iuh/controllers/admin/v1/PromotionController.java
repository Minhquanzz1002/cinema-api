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

import static vn.edu.iuh.constant.RouterConstant.*;
import static vn.edu.iuh.constant.SwaggerConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(AdminPaths.Promotion.BASE)
@RestController("promotionControllerAdminV1")
@Tag(name = "ADMIN V1: Promotion Management", description = "Quản lý khuyến mãi")
public class PromotionController {
    private final PromotionService promotionService;

    @Operation(summary = AdminSwagger.Promotion.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<Page<AdminPromotionResponseDTO>> getAllPromotions(
            @PageableDefault Pageable pageable,
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) BaseStatus status
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                promotionService.getAllPromotions(pageable, search, status, startDate, endDate)
        );
    }

    @Operation(summary = AdminSwagger.Promotion.GET_SUM)
    @GetMapping(AdminPaths.Promotion.DETAIL)
    public SuccessResponse<AdminPromotionOverviewProjection> getPromotion(@PathVariable String code) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                promotionService.getPromotionByCode(code)
        );
    }

    @Operation(summary = AdminSwagger.Promotion.CREATE_SUM)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SuccessResponse<Promotion> createPromotion(
            @RequestBody @Valid CreatePromotionRequestDTO createPromotionRequestDTO
    ) {
        return new SuccessResponse<>(
                201,
                "success",
                "Thêm chiến dịch khuyến mãi thành công",
                promotionService.createPromotion(createPromotionRequestDTO)
        );
    }

    @Operation(summary = AdminSwagger.Promotion.DELETE_SUM)
    @DeleteMapping(AdminPaths.Promotion.DELETE)
    public SuccessResponse<?> deletePromotion(@PathVariable int id) {
        promotionService.deletePromotionById(id);
        return new SuccessResponse<>(
                200,
                "success",
                "Xóa khuyến mãi thành công",
                null
        );
    }

    @Operation(summary = AdminSwagger.Promotion.UPDATE_SUM)
    @PutMapping(AdminPaths.Promotion.UPDATE)
    public SuccessResponse<Promotion> updatePromotion(
            @PathVariable int id,
            @RequestBody @Valid UpdatePromotionRequestDTO updatePromotionRequestDTO
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Cập nhật khuyến mãi thành công",
                promotionService.updatePromotion(id, updatePromotionRequestDTO)
        );
    }

    @Operation(summary = AdminSwagger.Promotion.GET_ALL_LINES_SUM)
    @GetMapping(AdminPaths.Promotion.GET_LINES)
    public SuccessResponse<Page<AdminPromotionLineOverviewProjection>> getPromotionLines(
            @PathVariable int id,
            @PageableDefault Pageable pageable
    ) {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                promotionService.getPromotionLines(id, pageable)
        );
    }

    @Operation(summary = AdminSwagger.Promotion.CREATE_LINES_SUM)
    @PostMapping(AdminPaths.Promotion.CREATE_LINES)
    public SuccessResponse<PromotionLine> createPromotionLine(
            @PathVariable int id,
            @RequestBody @Valid CreatePromotionLineRequestDTO createPromotionLineRequestDTO
    ) {
        return new SuccessResponse<>(
                201,
                "success",
                "Thêm chương trình khuyến mãi thành công",
                promotionService.createPromotionLine(id, createPromotionLineRequestDTO)
        );
    }
}
