package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.cinema.req.CreateCinemaRequest;
import vn.edu.iuh.dto.admin.v1.cinema.req.UpdateCinemaRequest;
import vn.edu.iuh.dto.client.v1.city.res.CityResponse;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.projections.v1.CinemaProjection;

import java.util.List;

public interface CinemaService {

    SuccessResponse<List<CinemaProjection>> getCinemas();

    Page<Cinema> getAllCinemas(String search, BaseStatus status, Pageable pageable);

    Cinema getCinemaById(Integer id);

    Cinema createCinema(CreateCinemaRequest request);

    Cinema updateCinema(Integer id, UpdateCinemaRequest request);

    void deleteCinema(Integer id);

    String generateCinemaCode();

    List<CityResponse> getCinemaCities();
}
