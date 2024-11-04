package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.req.CreateShowTimeRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeForSaleResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminShowTimeResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.ShowTimeFiltersResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.Room;
import vn.edu.iuh.models.ShowTime;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.projections.admin.v1.AdminCinemaFilterProjection;
import vn.edu.iuh.projections.admin.v1.AdminMovieFilterProjection;
import vn.edu.iuh.projections.admin.v1.AdminRoomNameOnlyProjection;
import vn.edu.iuh.projections.v1.ShowTimeProjection;
import vn.edu.iuh.repositories.CinemaRepository;
import vn.edu.iuh.repositories.MovieRepository;
import vn.edu.iuh.repositories.RoomRepository;
import vn.edu.iuh.repositories.ShowTimeRepository;
import vn.edu.iuh.services.ShowTimeService;
import vn.edu.iuh.specifications.GenericSpecifications;
import vn.edu.iuh.specifications.ShowTimeSpecification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        LocalTime currentTime;
        if (date.isEqual(LocalDate.now())) {
            currentTime = LocalTime.now();
        } else {
            currentTime = null;
        }

        if (cinemaId == null) {
            showTimes = showTimeRepository.findAllByMovieAndStartDateAndDeletedAndStatus(Movie.builder().id(movieId).build(), date, false, BaseStatus.ACTIVE, ShowTimeProjection.class);
        } else {
            showTimes = showTimeRepository.findAllByMovieAndStartDateAndCinemaAndDeletedAndStatus(Movie.builder().id(movieId).build(), date, Cinema.builder().id(cinemaId).build(), false, BaseStatus.ACTIVE, ShowTimeProjection.class);
        }

        if (currentTime != null) {
            showTimes = showTimes.stream()
                    .filter(showTime -> showTime.getStartTime().isAfter(currentTime))
                    .collect(Collectors.toList());
        }

        return new SuccessResponse<>(200, "success", "Thành công", showTimes);
    }

    @Override
    public AdminShowTimeResponseDTO getAllShowTimes(int cinemaId, LocalDate startDate, Integer movieId, BaseStatus status) {
        Specification<ShowTime> spec = Specification.where(ShowTimeSpecification.withCinema(cinemaId))
                .and(ShowTimeSpecification.onDate(startDate))
                .and(ShowTimeSpecification.withMovie(movieId))
                .and(GenericSpecifications.withStatus(status))
                .and(GenericSpecifications.withDeleted(false));
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

    @Override
    public void createShowTime(CreateShowTimeRequestDTO createShowTimeRequestDTO) {
        Movie movie = movieRepository.findByIdAndDeleted(createShowTimeRequestDTO.getMovieId(), false)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy phim"));
        Cinema cinema = cinemaRepository.findByIdAndDeleted(createShowTimeRequestDTO.getCinemaId(), false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy rạp"));
        Room room = roomRepository.findByIdAndDeleted(createShowTimeRequestDTO.getRoomId(), false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy phòng chiếu"));

        boolean hasConflict = showTimeRepository.existsByRoomAndStartDateAndStartTimeBetweenOrEndTimeBetween(
                room,
                createShowTimeRequestDTO.getStartDate(),
                createShowTimeRequestDTO.getStartTime(),
                createShowTimeRequestDTO.getEndTime()
        );

        if (hasConflict) {
            throw new BadRequestException("Đã có phim chiếu trong khung giờ này");
        }

        ShowTime showTime = ShowTime.builder()
                .movie(movie)
                .cinema(cinema)
                .room(room)
                .startDate(createShowTimeRequestDTO.getStartDate())
                .startTime(createShowTimeRequestDTO.getStartTime())
                .endTime(createShowTimeRequestDTO.getEndTime())
                .status(createShowTimeRequestDTO.getStatus())
                .build();
        showTimeRepository.save(showTime);
    }

    @Override
    public void deleteShowTime(UUID id) {
        ShowTime showTime = showTimeRepository.findByIdAndDeleted(id, false).orElseThrow(() -> new DataNotFoundException("Không tìm thấy lịch chiếu"));
        showTime.setDeleted(true);
        showTimeRepository.save(showTime);
    }

    @Override
    public List<AdminShowTimeForSaleResponseDTO> getShowTimesForSales(Integer cinemaId, Integer movieId, LocalDate date) {
        Specification<ShowTime> spec = Specification.where(ShowTimeSpecification.withCinema(cinemaId))
                .and(ShowTimeSpecification.onDate(date))
                .and(ShowTimeSpecification.withMovie(movieId))
                .and(GenericSpecifications.withDeleted(false));
        List<ShowTime> showTimes = showTimeRepository.findAll(spec);

        // If requested date is today, filter out show times that have already started
        LocalTime currentTime =  LocalTime.now();
        if (date.isEqual(LocalDate.now())) {
            showTimes = showTimes.stream()
                    .filter(showTime -> showTime.getStartTime().isAfter(currentTime))
                    .toList();
        }

        return showTimes.stream()
                .map(showTime -> modelMapper.map(showTime, AdminShowTimeForSaleResponseDTO.class))
                .toList();
    }
}
