package vn.edu.iuh.services;

import vn.edu.iuh.dto.admin.v1.showtime.req.BatchActivateShowTimeRequest;
import vn.edu.iuh.dto.admin.v1.showtime.req.CreateShowTimeRequest;
import vn.edu.iuh.dto.admin.v1.showtime.req.GenerateShowTimeRequest;
import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeForSaleResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeResponseDTO;
import vn.edu.iuh.dto.admin.v1.showtime.res.AdminShowTimeFilterResponse;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.models.ShowTime;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.projections.v1.ShowTimeProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ShowTimeService {
    ShowTime getById(UUID showTimeId);

    SuccessResponse<List<ShowTimeProjection>> getShowTimes(int movieId, LocalDate date, Integer cinemaId);

    AdminShowTimeResponseDTO getAllShowTimes(int cinemaId, LocalDate startDate, Integer movieId, BaseStatus status);

    AdminShowTimeFilterResponse getShowTimeFilters();

    void createShowTime(CreateShowTimeRequest request);

    void deleteShowTime(UUID id);

    List<AdminShowTimeForSaleResponseDTO> getShowTimesForSales(Integer cinemaId, Integer movieId, LocalDate date);

    /**
     * Automatically generate showtimes for the cinema within the specified time period.
     * This method will distribute and arrange showtimes based on the requested number of shows for each movie.
     * Scheduling rules:
     * - Operating hours: 8:00AM - 11:59PM
     * - Cleaning time between shows: 15 minutes
     *
     * @param body GenerateShowTimeRequestDTO
     */
    String generateShowTime(GenerateShowTimeRequest body);

    String activateMultipleShowTime(BatchActivateShowTimeRequest body);
}
