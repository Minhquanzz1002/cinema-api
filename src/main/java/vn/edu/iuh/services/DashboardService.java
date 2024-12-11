package vn.edu.iuh.services;

import vn.edu.iuh.dto.admin.v1.res.AdminCinemaRevenueSummaryResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminDashboardStatsResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieRevenueSummaryResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface DashboardService {
    /**
     * Retrieves all dashboard statistics for admin view
     * The statistics includes revenue, number of movies, number of shows, etc.
     *
     * @return AdminDashboardStatsResponseDTO contains aggregated statistics data
     */
    AdminDashboardStatsResponseDTO getStats();

    List<AdminMovieRevenueSummaryResponseDTO> getMovieRevenueSummary(
            LocalDate startDate,
            LocalDate endDate
    );

    List<AdminCinemaRevenueSummaryResponseDTO> getCinemaRevenueSummary(
            LocalDate startDate,
            LocalDate endDate
    );
}
