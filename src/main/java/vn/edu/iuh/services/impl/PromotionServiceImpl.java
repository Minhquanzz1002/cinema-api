package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.req.CreatePromotionLineRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreatePromotionRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdatePromotionRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminPromotionResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Promotion;
import vn.edu.iuh.models.PromotionLine;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.projections.admin.v1.AdminPromotionLineOverviewProjection;
import vn.edu.iuh.projections.admin.v1.AdminPromotionOverviewProjection;
import vn.edu.iuh.projections.v1.PromotionProjection;
import vn.edu.iuh.repositories.PromotionLineRepository;
import vn.edu.iuh.repositories.PromotionRepository;
import vn.edu.iuh.services.PromotionService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.PromotionSpecification;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionLineRepository promotionLineRepository;
    private final PromotionRepository promotionRepository;
    private final ModelMapper modelMapper;

    @Override
    public SuccessResponse<List<PromotionProjection>> getPromotions() {
        List<PromotionProjection> promotions = promotionRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false, PromotionProjection.class);
        return new SuccessResponse<>(200, "success", "Thành công", promotions);
    }

    @Override
    public AdminPromotionOverviewProjection getPromotionByCode(String code) {
        return promotionRepository.findByCodeAndDeleted(code, false, AdminPromotionOverviewProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy khuyến mãi"));
    }

    @Override
    public Page<AdminPromotionLineOverviewProjection> getPromotionLines(int promotionId, Pageable pageable) {
        return promotionLineRepository.findAllByPromotion_IdAndDeleted(promotionId, false, pageable, AdminPromotionLineOverviewProjection.class);
    }

    @Override
    public Page<AdminPromotionResponseDTO> getAllPromotions(Pageable pageable, String search, BaseStatus status, LocalDate startDate, LocalDate endDate) {
        Specification<Promotion> specification = Specification.where(null);
        specification = specification.and(PromotionSpecification.withNameOrCode(search))
                .and(GenericSpecifications.withStatus(status))
                .and(GenericSpecifications.withDeleted(false))
                .and(GenericSpecifications.betweenDates(startDate, endDate));
        Page<Promotion> promotions = promotionRepository.findAll(specification, pageable);
        return promotions.map(promotion -> modelMapper.map(promotion, AdminPromotionResponseDTO.class));
    }

    @Override
    public Promotion createPromotion(CreatePromotionRequestDTO createPromotionRequestDTO) {
        if (promotionRepository.existsOverlapping(createPromotionRequestDTO.getStartDate(), createPromotionRequestDTO.getEndDate())) {
            throw new BadRequestException("Đã tồn tại khuyến mãi trong khoảng thời gian này");
        }
        Promotion promotion = modelMapper.map(createPromotionRequestDTO, Promotion.class);
        return promotionRepository.save(promotion);
    }

    @Override
    public void deletePromotionById(int id) {
        Promotion promotion = getPromotionById(id);
        promotion.setDeleted(true);
        promotion.getPromotionLines().forEach(promotionLine -> {
            promotionLine.setDeleted(true);
            promotionLine.getPromotionDetails().forEach(promotionDetail -> promotionDetail.setDeleted(true));
        });
        promotionRepository.save(promotion);
    }

    @Override
    public Promotion updatePromotion(int id, UpdatePromotionRequestDTO updatePromotionRequestDTO) {
        Promotion promotion = getPromotionById(id);
        modelMapper.map(updatePromotionRequestDTO, promotion);
        return promotionRepository.save(promotion);
    }

    @Override
    public Promotion getPromotionById(int id) {
        return promotionRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy khuyến mãi"));
    }

    @Override
    public PromotionLine createPromotionLine(int promotionId, CreatePromotionLineRequestDTO createPromotionLineRequestDTO) {
        Promotion promotion = getPromotionById(promotionId);
        if (promotion.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Chiến dịch đang hoạt động không thể thêm chương trình");
        }
        PromotionLine promotionLine = modelMapper.map(createPromotionLineRequestDTO, PromotionLine.class);
        promotionLine.setPromotion(promotion);

        if (promotionLine.getPromotionDetails() != null) {
            promotionLine.getPromotionDetails().forEach(detail ->
                    detail.setPromotionLine(promotionLine)
            );
        }

        return promotionLineRepository.save(promotionLine);
    }
}
