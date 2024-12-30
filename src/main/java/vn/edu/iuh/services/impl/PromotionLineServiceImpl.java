package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.promotion.detail.req.CreatePromotionDetailRequest;
import vn.edu.iuh.dto.admin.v1.promotion.line.req.UpdatePromotionLineRequest;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.PromotionDetail;
import vn.edu.iuh.models.PromotionLine;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.PromotionLineRepository;
import vn.edu.iuh.services.PromotionLineService;

import java.time.LocalDate;

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
    public void createPromotionDetail(int promotionLineId, CreatePromotionDetailRequest request) {
        PromotionLine promotionLine = promotionLineRepository.findByIdAndDeleted(promotionLineId, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy mã khuyến mãi"));
        PromotionDetail promotionDetail = modelMapper.map(request, PromotionDetail.class);

        promotionLine.addPromotionDetail(promotionDetail);
        promotionLineRepository.save(promotionLine);
    }

    @Override
    public PromotionLine updatePromotionLine(int id, UpdatePromotionLineRequest request) {
        PromotionLine promotionLine = promotionLineRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy mã khuyến mãi"));

        LocalDate newStartDate = request.getStartDate();
        LocalDate newEndDate = request.getEndDate();

        // Check if start date is after end date
        if (newStartDate != null && newStartDate.isAfter(newEndDate)) {
            throw new BadRequestException("Ngày bắt đầu không thể sau ngày kết thúc");
        }

        // Check for overlapping promotions with same type
        if (promotionLineRepository.existsOverlappingPromotionLine(
                id,
                promotionLine.getType(),
                newStartDate != null ? newStartDate : promotionLine.getStartDate(),
                newEndDate
        )) {
            throw new BadRequestException("Đã tồn tại chương trình khuyến mãi cùng loại trong khoảng thời gian này");
        }

        if (promotionLine.getStatus() == BaseStatus.ACTIVE) {
            promotionLine.setEndDate(request.getEndDate());
        } else {
            modelMapper.map(request, promotionLine);
        }
        return promotionLineRepository.save(promotionLine);
    }
}
