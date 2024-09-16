package vn.edu.iuh.services;

import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.projections.CinemaProjection;

import java.util.List;

public interface CinemaService {
    SuccessResponse<List<CinemaProjection>> getCinemas();
}
