package vn.edu.iuh.services;

import vn.edu.iuh.dto.admin.v1.req.UpdatePromotionDetailRequestDTO;

public interface PromotionDetailService {
    void deletePromotionDetailById(int id);
    void updatePromotionDetailById(int id, UpdatePromotionDetailRequestDTO updatePromotionDetailRequestDTO);
}
