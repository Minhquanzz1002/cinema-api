package vn.edu.iuh.services;

import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.City;
import vn.edu.iuh.projections.CityProjection;

import java.util.List;

public interface CityService {
    SuccessResponse<List<CityProjection>> getCities();
}
