package vn.edu.iuh.services;

import vn.edu.iuh.dto.admin.v1.req.CreatePromotionDetailRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdatePromotionLineRequestDTO;
import vn.edu.iuh.models.PromotionLine;

public interface PromotionLineService {
    PromotionLine getPromotionLineByCode(String code);

    void deletePromotionLineById(int id);
    void createPromotionDetail(int promotionLineId, CreatePromotionDetailRequestDTO createPromotionDetailRequestDTO);
    PromotionLine updatePromotionLine(int id, UpdatePromotionLineRequestDTO updatePromotionLineRequestDTO);
}
