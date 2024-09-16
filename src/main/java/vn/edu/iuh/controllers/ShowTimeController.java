package vn.edu.iuh.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.projections.ShowTimeProjection;
import vn.edu.iuh.services.ShowTimeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/show-times")
public class ShowTimeController {
    private final ShowTimeService showTimeService;

    @GetMapping
    public SuccessResponse<List<ShowTimeProjection>> getShowTimes() {
        return showTimeService.getShowTimes();
    }
}
