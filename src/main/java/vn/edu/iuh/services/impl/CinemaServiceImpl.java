package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.projections.v1.CinemaProjection;
import vn.edu.iuh.repositories.CinemaRepository;
import vn.edu.iuh.services.CinemaService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository cinemaRepository;
    @Override
    public SuccessResponse<List<CinemaProjection>> getCinemas() {
        List<CinemaProjection> cinemas = cinemaRepository.findAllProjectionBy(CinemaProjection.class);
        return new SuccessResponse<>(200, "success", "Thành công", cinemas);
    }

    public String generateCinemaCode() {
        Optional<Cinema> lastCinema = cinemaRepository.findTopByOrderByCodeDesc();
        int nextNumber = 1;
        if (lastCinema.isPresent()) {
            String lastCode = lastCinema.get().getCode();
            nextNumber = Integer.parseInt(lastCode.substring(2)) + 1;
        }
        return String.format("CN%06d", nextNumber);
    }
}
