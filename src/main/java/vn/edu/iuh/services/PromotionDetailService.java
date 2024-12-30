package vn.edu.iuh.services;

import vn.edu.iuh.dto.admin.v1.promotion.detail.req.UpdatePromotionDetailRequest;

public interface PromotionDetailService {
    void deletePromotionDetailById(int id);
    void updatePromotionDetailById(int id, UpdatePromotionDetailRequest request);
}
