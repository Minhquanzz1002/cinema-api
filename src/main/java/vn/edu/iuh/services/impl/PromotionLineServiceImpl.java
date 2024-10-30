package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.req.CreatePromotionDetailRequestDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.PromotionDetail;
import vn.edu.iuh.models.PromotionLine;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.PromotionLineRepository;
import vn.edu.iuh.services.PromotionLineService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionLineServiceImpl implements PromotionLineService {
    private final PromotionLineRepository promotionLineRepository;
    private final ModelMapper modelMapper;
    @Override
    public PromotionLine getPromotionLineByCode(String code) {
        return promotionLineRepository.findByCodeAndDeleted(code, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy mã khuyến mãi"));
    }

    @Override
    public void deletePromotionLineById(int id) {
        PromotionLine promotionLine = promotionLineRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy mã khuyến mãi"));

        if (promotionLine.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể xóa khuyến mãi đang áp dụng");
        }

        promotionLine.setDeleted(true);
        promotionLineRepository.save(promotionLine);
    }

    @Override
    public void createPromotionDetail(int promotionLineId, CreatePromotionDetailRequestDTO createPromotionDetailRequestDTO) {
        PromotionLine promotionLine = promotionLineRepository.findByIdAndDeleted(promotionLineId, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy mã khuyến mãi"));
        PromotionDetail promotionDetail = modelMapper.map(createPromotionDetailRequestDTO, PromotionDetail.class);

        promotionLine.addPromotionDetail(promotionDetail);
        promotionLineRepository.save(promotionLine);
    }
}
