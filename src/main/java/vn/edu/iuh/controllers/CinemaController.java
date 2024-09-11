package vn.edu.iuh.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.services.CinemaService;

import java.util.List;

@RestController
@RequestMapping("/v1/cinemas")
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;

    @GetMapping
    public SuccessResponse<List<Cinema>> getCinemas() {
        return cinemaService.getCinemas();
    }

}
