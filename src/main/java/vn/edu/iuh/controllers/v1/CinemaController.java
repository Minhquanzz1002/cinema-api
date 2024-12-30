package vn.edu.iuh.controllers.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.constant.RouterConstant.ClientPaths;
import vn.edu.iuh.constant.SwaggerConstant.ClientSwagger;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.projections.v1.CinemaProjection;
import vn.edu.iuh.services.CinemaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ClientPaths.Cinema.BASE)
@Tag(name = "V1: Cinema Controller", description = "Quản lý rạp chiếu phim")
public class CinemaController {
    private final CinemaService cinemaService;

    @Operation(summary = ClientSwagger.Cinema.GET_ALL_SUM)
    @GetMapping
    public SuccessResponse<List<CinemaProjection>> getCinemas() {
        return cinemaService.getCinemas();
    }
}
