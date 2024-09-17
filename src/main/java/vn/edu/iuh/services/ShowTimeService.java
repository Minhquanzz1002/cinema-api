package vn.edu.iuh.services;

import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.ShowTimeProjection;

import java.time.LocalDate;
import java.util.List;

public interface ShowTimeService {
    SuccessResponse<List<ShowTimeProjection>> getShowTimes(int movieId, LocalDate date, Integer cinemaId);
}
