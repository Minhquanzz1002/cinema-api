package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Promotion;
import vn.edu.iuh.projections.admin.v1.AdminPromotionOverviewProjection;
import vn.edu.iuh.projections.v1.PromotionProjection;

import java.util.List;

public interface PromotionService {
    SuccessResponse<List<PromotionProjection>> getPromotions();
    Page<AdminPromotionOverviewProjection> getAllPromotions(Pageable pageable, String code, String name);
}
