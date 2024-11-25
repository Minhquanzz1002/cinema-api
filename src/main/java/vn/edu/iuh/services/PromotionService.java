package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import vn.edu.iuh.projections.v1.PromotionProjection;

import java.time.LocalDate;
import java.util.List;

public interface PromotionService {
    SuccessResponse<List<PromotionProjection>> getPromotions();

    AdminPromotionOverviewProjection getPromotionByCode(String code);

    Page<AdminPromotionLineOverviewProjection> getPromotionLines(int id, Pageable pageable);

    /**
     * Retrieves a paginated list of promotions based on filtering conditions
     *
     * @param pageable  Pageable containing pagination information (page number, page size, sort)
     * @param search    Search keyword (can be promotion code or name)
     * @param status    Status of promotion (ACTIVE, INACTIVE)
     * @param startDate Start date to filter promotions
     * @param endDate   End date to filter promotions
     * @return Page object containing the list of promotions filtered and paginated according to conditions
     */
    Page<AdminPromotionResponseDTO> getAllPromotions(
            Pageable pageable,
            String search,
            BaseStatus status,
            LocalDate startDate,
            LocalDate endDate
    );

    Promotion createPromotion(CreatePromotionRequestDTO createPromotionRequestDTO);

    void deletePromotionById(int id);

    Promotion updatePromotion(int id, UpdatePromotionRequestDTO updatePromotionRequestDTO);

    Promotion getPromotionById(int id);

    PromotionLine createPromotionLine(int promotionId, CreatePromotionLineRequestDTO createPromotionLineRequestDTO);
}
