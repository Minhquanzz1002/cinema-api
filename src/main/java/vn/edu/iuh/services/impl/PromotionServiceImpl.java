package vn.edu.iuh.services.impl;

import jakarta.transaction.Transactional;
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
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.Promotion;
import vn.edu.iuh.models.PromotionDetail;
import vn.edu.iuh.models.PromotionLine;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.models.enums.PromotionLineType;
import vn.edu.iuh.projections.admin.v1.AdminPromotionLineOverviewProjection;
import vn.edu.iuh.projections.admin.v1.AdminPromotionOverviewProjection;
import vn.edu.iuh.projections.v1.PromotionProjection;
import vn.edu.iuh.repositories.ProductRepository;
import vn.edu.iuh.repositories.PromotionLineRepository;
import vn.edu.iuh.repositories.PromotionRepository;
import vn.edu.iuh.services.PromotionService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.PromotionSpecification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionLineRepository promotionLineRepository;
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;
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
        LocalDate newStartDate = updatePromotionRequestDTO.getStartDate();
        LocalDate newEndDate = updatePromotionRequestDTO.getEndDate();

        if (promotion.getStatus() == BaseStatus.ACTIVE) {
            if (newEndDate.isBefore(promotion.getStartDate())) {
                throw new BadRequestException("Ngày kết thúc phải sau ngày bắt đầu");
            }
            promotion.setEndDate(newEndDate);
        } else {
            BaseStatus newStatus = updatePromotionRequestDTO.getStatus();
            if (newStatus == BaseStatus.ACTIVE) {
                boolean hasOverlap = promotionRepository.existsByDeletedAndStatusAndIdNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        false,
                        BaseStatus.ACTIVE,
                        id,
                        newEndDate,
                        newStartDate
                );
                if (hasOverlap) {
                    throw new BadRequestException("Đã tồn tại chiến dịch trong khoảng thời gian này");
                }
            }
            modelMapper.map(updatePromotionRequestDTO, promotion);
        }
        return promotionRepository.save(promotion);
    }

    @Override
    public Promotion getPromotionById(int id) {
        return promotionRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy khuyến mãi"));
    }

    @Transactional
    @Override
    public PromotionLine createPromotionLine(int promotionId, CreatePromotionLineRequestDTO createPromotionLineRequestDTO) {
        Promotion promotion = getPromotionById(promotionId);
//        if (promotion.getStatus() == BaseStatus.ACTIVE) {
//            throw new BadRequestException("Chiến dịch đang hoạt động không thể thêm chương trình");
//        }

        PromotionLineType type = createPromotionLineRequestDTO.getType();

        PromotionLine promotionLine = PromotionLine.builder()
                .promotion(promotion)
                .code(createPromotionLineRequestDTO.getCode())
                .name(createPromotionLineRequestDTO.getName())
                .type(type)
                .startDate(createPromotionLineRequestDTO.getStartDate())
                .endDate(createPromotionLineRequestDTO.getEndDate())
                .status(createPromotionLineRequestDTO.getStatus())
                .promotionDetails(new ArrayList<>())
                .build();

        switch (type) {
            case CASH_REBATE -> createPromotionLineRequestDTO.getPromotionDetails().forEach(detail -> {
                PromotionDetail promotionDetail = PromotionDetail.builder()
                        .discountValue(detail.getDiscountValue())
                        .minOrderValue(detail.getMinOrderValue())
                        .usageLimit(detail.getUsageLimit())
                        .status(detail.getStatus())
                        .build();
                promotionLine.addPromotionDetail(promotionDetail);
            });
            case PRICE_DISCOUNT -> createPromotionLineRequestDTO.getPromotionDetails().forEach(detail -> {
                PromotionDetail promotionDetail = PromotionDetail.builder()
                        .discountValue(detail.getDiscountValue())
                        .minOrderValue(detail.getMinOrderValue())
                        .maxDiscountValue(detail.getMaxDiscountValue())
                        .usageLimit(detail.getUsageLimit())
                        .status(detail.getStatus())
                        .build();
                promotionLine.addPromotionDetail(promotionDetail);
            });
            case BUY_TICKETS_GET_PRODUCTS -> createPromotionLineRequestDTO.getPromotionDetails().forEach(detail -> {
                Product product = productRepository.findByIdAndDeletedAndStatus(detail.getGiftProduct(), false, ProductStatus.ACTIVE).orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm"));

                PromotionDetail promotionDetail = PromotionDetail.builder()
                        .requiredSeatType(detail.getRequiredSeatType())
                        .requiredSeatQuantity(detail.getRequiredSeatQuantity())
                        .giftQuantity(detail.getGiftQuantity())
                        .giftProduct(product)
                        .usageLimit(detail.getUsageLimit())
                        .status(detail.getStatus())
                        .build();
                promotionLine.addPromotionDetail(promotionDetail);
            });
            case BUY_TICKETS_GET_TICKETS -> createPromotionLineRequestDTO.getPromotionDetails().forEach(detail -> {
                PromotionDetail promotionDetail = PromotionDetail.builder()
                        .requiredSeatType(detail.getRequiredSeatType())
                        .requiredSeatQuantity(detail.getRequiredSeatQuantity())
                        .giftSeatType(detail.getGiftSeatType())
                        .giftSeatQuantity(detail.getGiftSeatQuantity())
                        .usageLimit(detail.getUsageLimit())
                        .status(detail.getStatus())
                        .build();
                promotionLine.addPromotionDetail(promotionDetail);
            });
        }

        return promotionLineRepository.save(promotionLine);
    }
}
