package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.projections.ShowTimeProjection;
import vn.edu.iuh.repositories.ShowTimeRepository;
import vn.edu.iuh.services.ShowTimeService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowTimeServiceImpl implements ShowTimeService {
    private final ShowTimeRepository showTimeRepository;

    @Override
    public SuccessResponse<List<ShowTimeProjection>> getShowTimes(int movieId, LocalDate date, Integer cinemaId) {
        List<ShowTimeProjection> showTimes;
        if (cinemaId == null) {
            showTimes = showTimeRepository.findAllByMovieAndStartDate(Movie.builder().id(movieId).build(), date, ShowTimeProjection.class);
        } else {
            showTimes = showTimeRepository.findAllByMovieAndStartDateAndCinema(Movie.builder().id(movieId).build(), date, Cinema.builder().id(cinemaId).build(), ShowTimeProjection.class);
        }
        return new SuccessResponse<>(200, "success", "Thành công", showTimes);
    }
}
