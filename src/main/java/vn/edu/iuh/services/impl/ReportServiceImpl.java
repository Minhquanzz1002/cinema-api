package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.res.AdminDailyReportResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminPromotionSummaryResponseDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.models.PromotionLine;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.repositories.OrderRepository;
import vn.edu.iuh.repositories.PromotionLineRepository;
import vn.edu.iuh.services.ReportService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.PromotionLineSpecification;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final OrderRepository orderRepository;
    private final PromotionLineRepository promotionLineRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<AdminDailyReportResponseDTO> getDailyReport(LocalDate fromDate, LocalDate toDate) {
        LocalDate now = LocalDate.now();

        LocalDate effectiveFromDate = fromDate;
        LocalDate effectiveToDate = toDate;
        if (fromDate == null && toDate == null) {
            effectiveFromDate = now.withDayOfMonth(1);
            effectiveToDate = now;
        } else if (fromDate == null) {
            effectiveFromDate = toDate.withDayOfMonth(1);
        } else if (toDate == null) {
            effectiveToDate = now;
        }
        if (effectiveFromDate.isAfter(effectiveToDate)) {
            throw new BadRequestException("Ngày bắt đầu không thể sau ngày kết thúc");
        }
        return orderRepository.getDailyReport(effectiveFromDate, effectiveToDate);
    }

    @Override
    public List<AdminPromotionSummaryResponseDTO> getPromotionReport(LocalDate fromDate, LocalDate toDate, String code) {
        Specification<PromotionLine> spec = Specification.where(null);
        spec = spec.and(GenericSpecifications.betweenDates(fromDate, toDate));
        spec = spec.and(GenericSpecifications.withStatus(BaseStatus.ACTIVE));
        spec = spec.and(PromotionLineSpecification.withCode(code));
        spec = spec.and(PromotionLineSpecification.withActivePromotion());

        List<PromotionLine> promotionLines = promotionLineRepository.findAll(spec);
        return promotionLines.stream()
                .map(promotionLine -> modelMapper.map(promotionLine, AdminPromotionSummaryResponseDTO.class))
                .toList();
    }
}
