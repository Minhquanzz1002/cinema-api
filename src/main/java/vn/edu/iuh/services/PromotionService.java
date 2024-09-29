package vn.edu.iuh.services;

import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Promotion;
import vn.edu.iuh.projections.v1.PromotionProjection;

import java.util.List;

public interface PromotionService {
    SuccessResponse<List<PromotionProjection>> getPromotions();
}
