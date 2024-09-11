package vn.edu.iuh.services;

import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Cinema;

import java.util.List;

public interface CinemaService {
    SuccessResponse<List<Cinema>> getCinemas();
}
