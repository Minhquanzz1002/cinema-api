package vn.edu.iuh.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.GenreService;

import java.util.List;

@RestController
@RequestMapping("/v1/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public SuccessResponse<List<?>> getGenres() {
        return genreService.getGenres();
    }
}
