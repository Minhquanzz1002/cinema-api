package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.req.ActivateMultipleShowTimeRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.CreateShowTimeRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.GenerateShowTimeRequestDTO;
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
import vn.edu.iuh.utils.MovieRotation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowTimeServiceImpl implements ShowTimeService {
    private final ShowTimeRepository showTimeRepository;
    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final MovieRotationFactory movieRotationFactory;
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
        LocalTime currentTime = LocalTime.now();
        if (date.isEqual(LocalDate.now())) {
            showTimes = showTimes.stream()
                    .filter(showTime -> showTime.getStartTime().isAfter(currentTime))
                    .toList();
        }

        return showTimes.stream()
                .map(showTime -> modelMapper.map(showTime, AdminShowTimeForSaleResponseDTO.class))
                .toList();
    }

    @Override
    public void generateShowTime(GenerateShowTimeRequestDTO body) {
        LocalDate startDate = body.getStartDate();
        LocalDate endDate = body.getEndDate();

        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("Thời gian kết thúc phải lớn hơn thời gian bắt đầu");
        }

        Cinema cinema = cinemaRepository.findByIdAndDeleted(body.getCinemaId(), false)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy rạp"));

        List<Room> rooms = roomRepository.findByCinemaAndDeleted(cinema, false);
        if (rooms.isEmpty()) {
            throw new DataNotFoundException("Rạp không có phòng chiếu");
        }

        List<ShowTime> existingShowTimes = showTimeRepository.findByCinemaAndDeletedAndStartDateBetween(
                cinema,
                false,
                startDate,
                endDate
        );

        List<Movie> movies = movieRepository.findAllByIdInAndDeleted(
                body.getMovies().stream().map(GenerateShowTimeRequestDTO.MovieDTO::getId).toList(),
                false
        );
        if (movies.size() != body.getMovies().size()) {
            throw new BadRequestException("Một số phim không tồn tại hoặc bị xóa");
        }

        LocalTime startDayTime = LocalTime.of(8, 0);
        LocalTime endDayTime = LocalTime.of(23, 59);
        int CLEANING_TIME = 15;

        Map<Movie, Integer> remainingShowTimes = body.getMovies().stream()
                .collect(Collectors.toMap(
                        movieDTO -> movies.stream().filter(movie -> movie.getId() == movieDTO.getId()).findFirst().get(),
                        GenerateShowTimeRequestDTO.MovieDTO::getTotalShowTimes
                ));

        MovieRotation rotation = movieRotationFactory.createRotation(
                movies.stream().collect(Collectors.toMap(
                        movie -> movie,
                        movie -> body.getMovies().stream()
                                .filter(m -> m.getId() == movie.getId())
                                .findFirst()
                                .get()
                                .getTotalShowTimes()
                )),
                remainingShowTimes
        );

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            final LocalDate checkDate = currentDate;

            for (Room room : rooms) {
                log.info("Generating show times for room {} on date {}", room.getName(), checkDate);
                LocalTime currentTime = startDayTime;

                int minDuration = movies.stream().mapToInt(Movie::getDuration).min().orElse(0);

                while (currentTime.isBefore(endDayTime) && !remainingShowTimes.isEmpty()) {
                    log.info("Current time: {}", currentTime);
                    LocalTime potentialEndTime = currentTime.plusMinutes(minDuration + CLEANING_TIME);
                    if (potentialEndTime.isBefore(currentTime) || potentialEndTime.plusMinutes(5).isAfter(endDayTime)) {
                        log.info("End of day reached");
                        break;
                    }

                    Movie selectedMovie = findSuitableMovie(
                            rotation,
                            currentTime,
                            endDayTime,
                            room,
                            checkDate,
                            existingShowTimes,
                            CLEANING_TIME
                    );

                    if (selectedMovie == null) {
                        LocalTime newTime = currentTime.plusMinutes(15);

                        if (!newTime.isAfter(currentTime)) {
                            log.info("Time not advancing, breaking loop");
                            break;
                        }

                        currentTime = newTime;
                        potentialEndTime = currentTime.plusMinutes(minDuration + CLEANING_TIME);
                        if (potentialEndTime.plusMinutes(5).isAfter(endDayTime)) {
                            log.info("End of day reached 2");
                            break;
                        }

                        continue;
                    }

                    LocalTime endTime = currentTime.plusMinutes(selectedMovie.getDuration() + CLEANING_TIME);
                    endTime = roundToNearestInterval(endTime, 5);
                    LocalTime nextStartTime = endTime.plusMinutes(5);

                    ShowTime showTime = ShowTime.builder()
                            .movie(selectedMovie)
                            .room(room)
                            .cinema(cinema)
                            .startDate(checkDate)
                            .startTime(currentTime)
                            .endTime(endTime)
                            .totalSeat(0)
                            .bookedSeat(0)
                            .status(BaseStatus.INACTIVE)
                            .build();

                    showTimeRepository.save(showTime);
                    existingShowTimes.add(showTime);

                    int remaining = remainingShowTimes.get(selectedMovie) - 1;
                    if (remaining == 0) {
                        remainingShowTimes.remove(selectedMovie);
                    } else {
                        remainingShowTimes.put(selectedMovie, remaining);
                    }

                    currentTime = nextStartTime;
                }
            }

            currentDate = currentDate.plusDays(1);
        }


    }

    @Override
    public String activateMultipleShowTime(ActivateMultipleShowTimeRequestDTO body) {
        List<ShowTime> showTimes = showTimeRepository.findAllByCinema_IdAndStartDateAndRoom_IdInAndMovie_IdInAndStatusAndDeleted(
                body.getCinemaId(),
                body.getStartDate(),
                body.getRoomIds(),
                body.getMovieIds(),
                BaseStatus.INACTIVE,
                false
        );

        showTimes.forEach(showTime -> showTime.setStatus(BaseStatus.ACTIVE));
        showTimeRepository.saveAll(showTimes);
        return String.format("Đã kích hoạt %d lịch chiếu", showTimes.size());
    }

    private Movie findSuitableMovie(
            MovieRotation rotation,
            LocalTime currentTime,
            LocalTime endDayTime,
            Room room,
            LocalDate checkDate,
            List<ShowTime> existingShowTimes,
            int cleaningTime
    ) {
        Set<Movie> triedMovies = new HashSet<>();
        while (true) {
            Movie movie = rotation.getNextMovie();
            if (movie == null || triedMovies.contains(movie)) {
                return null;
            }

            triedMovies.add(movie);

            LocalTime endTime = currentTime.plusMinutes(movie.getDuration() + cleaningTime);
            endTime = roundToNearestInterval(endTime, 5);
            LocalTime nextStartTime = endTime.plusMinutes(5);

            if (endTime.getHour() == 23 && endTime.getMinute() > 59) {
                return null;
            }

            if (endTime.getHour() > 23) {
                return null;
            }

            if (endTime.isAfter(endDayTime)) {
                return null;
            }

            if (nextStartTime.isAfter(endDayTime)) {
                return null;
            }

            LocalTime finalEndTime = endTime;
            boolean hasConflict = existingShowTimes.stream().anyMatch(st ->
                    st.getRoom().equals(room) &&
                    st.getStartDate().equals(checkDate) &&
                    (
                            // Kiểm tra thời gian bắt đầu nằm trong khoảng của suất đã tồn tại
                            (currentTime.equals(st.getStartTime()) || currentTime.isAfter(st.getStartTime())) &&
                            currentTime.isBefore(st.getEndTime()) ||

                            // Kiểm tra thời gian kết thúc nằm trong khoảng của suất đã tồn tại
                            (finalEndTime.isAfter(st.getStartTime()) &&
                             (finalEndTime.isBefore(st.getEndTime()) || finalEndTime.equals(st.getEndTime()))) ||

                            // Kiểm tra suất mới bao trọn suất đã tồn tại
                            (currentTime.isBefore(st.getStartTime()) && finalEndTime.isAfter(st.getEndTime())) ||

                            // Kiểm tra thời gian bắt đầu trùng nhau
                            currentTime.equals(st.getStartTime())
                    )
            );

            if (!hasConflict) {
                return movie;
            }
        }
    }

    private LocalTime roundToNearestInterval(LocalTime time, int minuteInterval) {
        int minutes = time.getHour() * 60 + time.getMinute();
        int roundedMinutes = (int) (Math.ceil((double) minutes / minuteInterval) * minuteInterval);

        if (roundedMinutes >= 24 * 60) {
            return LocalTime.of(23, 59);
        }

        return LocalTime.of(roundedMinutes / 60, roundedMinutes % 60);
    }
}
