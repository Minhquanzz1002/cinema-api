package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.iuh.dto.admin.v1.req.CreateCinemaRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateCinemaRequestDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminCinemaDetailResponseDTO;
import vn.edu.iuh.dto.res.CityResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.projections.v1.CinemaProjection;

import java.util.List;

public interface CinemaService {

    SuccessResponse<List<CinemaProjection>> getCinemas();

    Page<Cinema> getAllCinemas(String search, BaseStatus status, Pageable pageable);

    Cinema getCinemaById(Integer id);

    Cinema createCinema(CreateCinemaRequestDTO request);

    Cinema updateCinema(Integer id, UpdateCinemaRequestDTO request);

    void deleteCinema(Integer id);

    String generateCinemaCode();

    List<CityResponseDTO> getCinemaCities();
}
