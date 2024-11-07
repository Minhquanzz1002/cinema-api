package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.res.AdminDailyReportResponseDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.repositories.OrderRepository;
import vn.edu.iuh.services.ReportService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final OrderRepository orderRepository;

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
        log.info("From Date: {}, To Date: {}", effectiveFromDate, effectiveToDate);

        return orderRepository.getDailyReport(effectiveFromDate, effectiveToDate);
    }
}
