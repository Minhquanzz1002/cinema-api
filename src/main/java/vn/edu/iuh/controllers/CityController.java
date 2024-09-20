package vn.edu.iuh.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Location Controller", description = "Quản lý thành phố")
public class CityController {
    private final CityService cityService;

    @Operation(
            summary = "Danh sách thành phố có rạp"
    )
    @GetMapping
    public SuccessResponse<List<CityProjection>> getCities() {
        return cityService.getCities();
    }
}
