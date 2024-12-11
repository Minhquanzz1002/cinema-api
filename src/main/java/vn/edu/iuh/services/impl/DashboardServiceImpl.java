package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.admin.v1.res.AdminCinemaRevenueSummaryResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminDashboardStatsResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieRevenueSummaryResponseDTO;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.MovieStatus;
import vn.edu.iuh.models.enums.OrderStatus;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.repositories.*;
import vn.edu.iuh.services.DashboardService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final MovieRepository movieRepository;
    private final ShowTimeRepository showTimeRepository;
    private final OrderRepository orderRepository;
    private final CinemaRepository cinemaRepository;
    private final UserRepository userRepository;

    @Override
    public AdminDashboardStatsResponseDTO getStats() {
        LocalDate today = LocalDate.now();

        int moviesCount = movieRepository.countAllByStatusAndDeleted(MovieStatus.ACTIVE, false);
        int showTimesToday = showTimeRepository.countAllByStatusAndDeletedAndStartDate(
                BaseStatus.ACTIVE, false,
                today
        );

        List<Order> orders = orderRepository.findAllByStatusAndDeletedAndOrderDateBetween(
                OrderStatus.COMPLETED,
                false,
                today.atStartOfDay(),
                today.atTime(23, 59, 59)
        );

        float totalRevenue = orders.stream()
                                   .map(Order::getTotalPrice)
                                   .reduce(0f, Float::sum);

        int totalCinemas = cinemaRepository.countAllByStatusAndDeleted(BaseStatus.ACTIVE, false);

        int totalCustomers = userRepository.countAllByCodeStartingWithAndStatusAndDeleted(
                "USER",
                UserStatus.ACTIVE,
                false
        );

        int totalEmployees = userRepository.countAllByCodeStartingWithAndStatusAndDeleted(
                "NVBH",
                UserStatus.ACTIVE,
                false
        );

        return AdminDashboardStatsResponseDTO.builder()
                                             .moviesCount(moviesCount)
                                             .showTimesToday(showTimesToday)
                                             .totalRevenue(totalRevenue)
                                             .totalCinemas(totalCinemas)
                                             .totalCustomers(totalCustomers)
                                             .totalEmployees(totalEmployees)
                                             .build();
    }

    @Override
    public List<AdminMovieRevenueSummaryResponseDTO> getMovieRevenueSummary(LocalDate startDate, LocalDate endDate) {
        List<Movie> movies = movieRepository.findAllByStatusAndDeleted(MovieStatus.ACTIVE, false);
        if (movies.isEmpty()) {
            throw new BadRequestException("Không tìm thấy phim nào");
        }
        List<Order> orders = orderRepository.findAllByStatusAndDeletedAndOrderDateBetween(
                OrderStatus.COMPLETED,
                false,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
        Map<String, Float> revenueByMovie = movies.stream()
                                                  .collect(Collectors.toMap(
                                                          Movie::getTitle,
                                                          movie -> 0f
                                                  ));

        for (Order order : orders) {
            String movieName = order.getShowTime().getMovie().getTitle();
            float revenue = order.getFinalAmount();
            revenueByMovie.merge(movieName, revenue, Float::sum);
        }

        return revenueByMovie.entrySet().stream()
                             .map(entry -> new AdminMovieRevenueSummaryResponseDTO(
                                     entry.getKey(),
                                     entry.getValue()
                             ))
                             .sorted((a, b) -> (int) (b.getTotalRevenue() - a.getTotalRevenue()))
                             .collect(Collectors.toList());
    }

    @Override
    public List<AdminCinemaRevenueSummaryResponseDTO> getCinemaRevenueSummary(LocalDate startDate, LocalDate endDate) {
        List<Cinema> cinemas = cinemaRepository.findAllByStatusAndDeleted(BaseStatus.ACTIVE, false);
        if (cinemas.isEmpty()) {
            throw new BadRequestException("Không tìm thấy rạp nào");
        }
        List<Order> orders = orderRepository.findAllByStatusAndDeletedAndOrderDateBetween(
                OrderStatus.COMPLETED,
                false,
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
        Map<String, Float> revenueByCinema = cinemas.stream()
                                                  .collect(Collectors.toMap(
                                                          Cinema::getName,
                                                          cinema -> 0f
                                                  ));
        for (Order order : orders) {
            String cinemaName = order.getShowTime().getCinema().getName();
            float revenue = order.getFinalAmount();
            revenueByCinema.merge(cinemaName, revenue, Float::sum);
        }
        return revenueByCinema.entrySet().stream()
                .map(entry -> new AdminCinemaRevenueSummaryResponseDTO(
                        entry.getKey(),
                        entry.getValue()
                ))
                .sorted((a, b) -> (int) (b.getTotalRevenue() - a.getTotalRevenue()))
                .collect(Collectors.toList());
    }
}
