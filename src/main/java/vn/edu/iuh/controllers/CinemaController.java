package vn.edu.iuh.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.projections.CinemaProjection;
import vn.edu.iuh.services.CinemaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cinemas")
@Tag(name = "Cinema Controller", description = "Quản lý rạp chiếu phim")
public class CinemaController {
    private final CinemaService cinemaService;

    @Operation(
            summary = "Danh sách rạp chiếu phim"
    )
    @GetMapping
    public SuccessResponse<List<CinemaProjection>> getCinemas() {
        return cinemaService.getCinemas();
    }
}
