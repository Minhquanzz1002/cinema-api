package vn.edu.iuh.services;


import vn.edu.iuh.dto.admin.v1.res.AdminDailyReportResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<AdminDailyReportResponseDTO> getDailyReport(LocalDate fromDate, LocalDate toDate);
}
