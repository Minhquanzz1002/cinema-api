package vn.edu.iuh.services.impl;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.dto.admin.v1.req.CreateCinemaRequestDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateCinemaRequestDTO;
import vn.edu.iuh.dto.res.CityResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.projections.v1.CinemaProjection;
import vn.edu.iuh.repositories.CinemaRepository;
import vn.edu.iuh.services.CinemaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository cinemaRepository;
    private final Slugify slugify;

    @Override
    public SuccessResponse<List<CinemaProjection>> getCinemas() {
        List<CinemaProjection> cinemas = cinemaRepository.findAllProjectionBy(CinemaProjection.class);
        return new SuccessResponse<>(200, "success", "Thành công", cinemas);
    }

    @Override
    public Page<Cinema> getAllCinemas(String search, BaseStatus status, Pageable pageable) {
        Specification<Cinema> spec = Specification.where(null);

        // Add search condition if search parameter is provided
        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("code")), "%" + search.toLowerCase() + "%")
                    )
            );
        }

        // Add status condition if status parameter is provided
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        return cinemaRepository.findAll(spec, pageable);
    }

    @Override
    public Cinema getCinemaById(Integer id) {
        return cinemaRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy rạp với id: " + id));
    }

    @Override
    @Transactional
    public Cinema createCinema(CreateCinemaRequestDTO request) {
        Cinema cinema = Cinema.builder()
                .code(generateCinemaCode())
                .name(request.getName())
                .address(request.getAddress())
                .ward(request.getWard())
                .wardCode(request.getWardCode())
                .district(request.getDistrict())
                .districtCode(request.getDistrictCode())
                .city(request.getCity())
                .cityCode(request.getCityCode())
                .images(request.getImages() != null ? request.getImages() : new ArrayList<>())
                .hotline(request.getHotline())
                .slug(slugify.slugify(request.getName()))
                .status(BaseStatus.ACTIVE)
                .build();

        return cinemaRepository.save(cinema);
    }

    @Override
    @Transactional
    public Cinema updateCinema(Integer id, UpdateCinemaRequestDTO request) {
        Cinema cinema = getCinemaById(id);

        // Update fields
        cinema.setName(request.getName());
        cinema.setAddress(request.getAddress());
        cinema.setWard(request.getWard());
        cinema.setDistrict(request.getDistrict());
        if (request.getImages() != null) {
            cinema.setImages(request.getImages());
        }
        cinema.setHotline(request.getHotline());
        cinema.setSlug(slugify.slugify(request.getName()));
        if (request.getStatus() != null) {
            cinema.setStatus(request.getStatus());
        }

        return cinemaRepository.save(cinema);
    }

    @Override
    public void deleteCinema(Integer id) {
        Cinema cinema = getCinemaById(id);
        cinema.setDeleted(true);
        cinemaRepository.save(cinema);
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

    @Override
    public List<CityResponseDTO> getCinemaCities() {
        List<CityResponseDTO> cities = cinemaRepository.findDistinctCities(BaseStatus.ACTIVE, false);
        return cities.stream()
                     .peek(city -> city.setName(removeCityPrefix(city.getName())))
                     .collect(Collectors.toList());
    }

    private String removeCityPrefix(String name) {
        return name.replace("Thành phố ", "").replace("Tỉnh ", "");
    }
}