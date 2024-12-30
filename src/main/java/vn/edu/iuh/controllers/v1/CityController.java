package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.constant.RouterConstant.ClientPaths;
import vn.edu.iuh.constant.SwaggerConstant.ClientSwagger;
import vn.edu.iuh.dto.client.v1.city.res.CityResponse;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.services.CinemaService;

import java.util.List;

@RestController
@RequestMapping(ClientPaths.City.BASE)
@RequiredArgsConstructor
@Tag(name = "V1: Location Controller", description = "Quản lý thành phố")
public class CityController {
    private final CinemaService cinemaService;

    @Operation(summary = ClientSwagger.City.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<List<CityResponse>> getCities() {
        return new SuccessResponse<>(
                200,
                "success",
                "Thành công",
                cinemaService.getCinemaCities()
        );
    }
}
