package vn.edu.iuh.services;


import vn.edu.iuh.dto.admin.v1.res.AdminDailyReportResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminPromotionSummaryResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<AdminDailyReportResponseDTO> getDailyReport(LocalDate fromDate, LocalDate toDate);

    List<AdminPromotionSummaryResponseDTO> getPromotionReport(LocalDate fromDate, LocalDate toDate, String code);
}
