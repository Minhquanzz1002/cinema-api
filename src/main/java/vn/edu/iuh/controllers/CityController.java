package vn.edu.iuh.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.CityProjection;
import vn.edu.iuh.services.CityService;

import java.util.List;

@RestController
@RequestMapping("/v1/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping
    public SuccessResponse<List<CityProjection>> getCities() {
        return cityService.getCities();
    }
}
