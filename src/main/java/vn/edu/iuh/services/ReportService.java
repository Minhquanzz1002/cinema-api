package vn.edu.iuh.services;


import vn.edu.iuh.dto.admin.v1.res.AdminEmployeeReportResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieReportResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminPromotionSummaryResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    /**
     * Retrieve sales report data grouped by employees within a specified date range.
     * The report includes sales performance metrics for each employee.
     *
     * @param fromDate The start date of the reporting period (inclusive)
     * @param toDate   The end date of the reporting period (inclusive)
     * @param search   Search term to filter employees by name or ID
     * @return List of employee sales report containing individual sales performance data
     */
    List<AdminEmployeeReportResponseDTO> getEmployeeSalesPerformanceReport(
            LocalDate fromDate,
            LocalDate toDate,
            String search
    );

    /**
     * Generates a summary report of promotions and their performance metrics.
     *
     * @param fromDate The start date for analyzing promotions (inclusive)
     * @param toDate   The end date for analyzing promotions (inclusive)
     * @param code     The code to filter specific promotions (optional)
     * @return List of promotion summary report containing performance metrics
     */
    List<AdminPromotionSummaryResponseDTO> getPromotionReport(LocalDate fromDate, LocalDate toDate, String code);

    /**
     * Retrieve sales report data grouped by movies within a specified date range.
     *
     * @param fromDate The start date of the reporting period (inclusive)
     * @param toDate   The end date of the reporting period (inclusive)
     * @param search   Search term to filter movies by title or code
     * @return List of movie sales report containing sales performance data
     */
    List<AdminMovieReportResponseDTO> getMovieSalesPerformanceReport(
            LocalDate fromDate,
            LocalDate toDate,
            String search
    );
}
