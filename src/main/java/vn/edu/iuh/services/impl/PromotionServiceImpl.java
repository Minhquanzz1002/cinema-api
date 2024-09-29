package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.projections.v1.PromotionProjection;
import vn.edu.iuh.repositories.PromotionRepository;
import vn.edu.iuh.services.PromotionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;

    @Override
    public SuccessResponse<List<PromotionProjection>> getPromotions() {
        List<PromotionProjection> promotions = promotionRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, PromotionProjection.class);
        return new SuccessResponse<>(200, "success", "Thành công", promotions);
    }
}
