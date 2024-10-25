package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.ShowTimeFiltersResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.ShowTime;
import vn.edu.iuh.projections.admin.v1.AdminCinemaFilterProjection;
import vn.edu.iuh.projections.admin.v1.AdminMovieFilterProjection;
import vn.edu.iuh.projections.admin.v1.AdminRoomNameOnlyProjection;
import vn.edu.iuh.projections.v1.ShowTimeProjection;
import vn.edu.iuh.repositories.CinemaRepository;
import vn.edu.iuh.repositories.MovieRepository;
import vn.edu.iuh.repositories.RoomRepository;
import vn.edu.iuh.repositories.ShowTimeRepository;
import vn.edu.iuh.services.ShowTimeService;
import vn.edu.iuh.specifications.ShowTimeSpecification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowTimeServiceImpl implements ShowTimeService {
    private final ShowTimeRepository showTimeRepository;
    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Override
    public ShowTime getById(UUID showTimeId) {
        return showTimeRepository.findById(showTimeId).orElseThrow(() -> new DataNotFoundException("Không tìm thấy lịch chiếu"));
    }

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

    @Override
    public AdminShowTimeResponseDTO getAllShowTimes(int cinemaId, LocalDate startDate) {
        Specification<ShowTime> spec = Specification.where(ShowTimeSpecification.withCinema(cinemaId))
                .and(ShowTimeSpecification.onDate(startDate));
        List<ShowTime> showTimes = showTimeRepository.findAll(spec);
        List<AdminShowTimeResponseDTO.ShowTimeDTO> showTimeProjections = showTimes.stream()
                .map(showTime -> modelMapper.map(showTime, AdminShowTimeResponseDTO.ShowTimeDTO.class))
                .toList();
        return AdminShowTimeResponseDTO.builder()
                .showTimes(showTimeProjections)
                .rooms(roomRepository.findAllProjectionByDeletedAndCinema_Id(false, cinemaId, AdminRoomNameOnlyProjection.class))
                .build();
    }

    @Override
    public ShowTimeFiltersResponseDTO getShowTimeFilters() {
        List<AdminCinemaFilterProjection> cinemas = cinemaRepository.findAllProjectionByDeleted(false, AdminCinemaFilterProjection.class);
        List<AdminMovieFilterProjection> movies = movieRepository.findAllProjectionByDeleted(false, AdminMovieFilterProjection.class);
        return ShowTimeFiltersResponseDTO.builder()
                .cinemas(cinemas)
                .movies(movies)
                .build();
    }
}
