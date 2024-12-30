package vn.edu.iuh.services;

import vn.edu.iuh.dto.admin.v1.promotion.detail.req.CreatePromotionDetailRequest;
import vn.edu.iuh.dto.admin.v1.promotion.line.req.UpdatePromotionLineRequest;
import vn.edu.iuh.models.PromotionLine;

public interface PromotionLineService {
    PromotionLine getPromotionLineByCode(String code);

    void deletePromotionLineById(int id);
    void createPromotionDetail(int promotionLineId, CreatePromotionDetailRequest request);
    PromotionLine updatePromotionLine(int id, UpdatePromotionLineRequest request);
}
