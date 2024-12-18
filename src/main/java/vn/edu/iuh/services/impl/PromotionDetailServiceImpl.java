package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import vn.edu.iuh.dto.admin.v1.req.UpdatePromotionDetailRequestDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.PromotionDetail;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.PromotionDetailRepository;
import vn.edu.iuh.services.PromotionDetailService;

@Service
@RequiredArgsConstructor
public class PromotionDetailServiceImpl implements PromotionDetailService {
    private final PromotionDetailRepository promotionDetailRepository;
    private final ModelMapper modelMapper;


    @Override
    public void deletePromotionDetailById(int id) {
        PromotionDetail promotionDetail = promotionDetailRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết chương trình giảm giá"));
        if (promotionDetail.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể xóa chi tiết chương trình giảm giá đang áp dụng");
        }

        promotionDetail.setDeleted(true);
        promotionDetail.setStatus(BaseStatus.INACTIVE);
        promotionDetailRepository.save(promotionDetail);
    }

    @Override
    public void updatePromotionDetailById(int id, UpdatePromotionDetailRequestDTO updatePromotionDetailRequestDTO) {
        PromotionDetail detail = promotionDetailRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết chương trình giảm giá"));

        modelMapper.map(updatePromotionDetailRequestDTO, detail);
        promotionDetailRepository.save(detail);
    }
}
