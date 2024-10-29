package vn.edu.iuh.services;

import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.ShowTimeFiltersResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
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
    ShowTimeFiltersResponseDTO getShowTimeFilters();

}
