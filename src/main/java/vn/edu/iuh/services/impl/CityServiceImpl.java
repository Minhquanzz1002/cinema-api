package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.v1.CityProjection;
import vn.edu.iuh.repositories.CityRepository;
import vn.edu.iuh.services.CityService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    @Override
    public SuccessResponse<List<CityProjection>> getCities() {
        List<CityProjection> cities = cityRepository.findAllProjectionBy();
        return new SuccessResponse<>(200, "success", "Thành công", cities);
    }
}
