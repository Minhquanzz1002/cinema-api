package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.dto.admin.v1.cinema.req.CreateCinemaRequest;
import vn.edu.iuh.dto.admin.v1.cinema.req.UpdateCinemaRequest;
import vn.edu.iuh.dto.client.v1.city.res.CityResponse;
import vn.edu.iuh.dto.common.SuccessResponse;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.Room;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.projections.v1.CinemaProjection;
import vn.edu.iuh.repositories.CinemaRepository;
import vn.edu.iuh.services.CinemaService;
import vn.edu.iuh.services.SlugifyService;
import vn.edu.iuh.specifications.GenericSpecifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository cinemaRepository;
    private final SlugifyService slugifyService;

    @Override
    public SuccessResponse<List<CinemaProjection>> getCinemas() {
        List<CinemaProjection> cinemas = cinemaRepository.findAllProjectionBy(CinemaProjection.class);
        return new SuccessResponse<>(200, "success", "Thành công", cinemas);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Cinema> getAllCinemas(String search, BaseStatus status, Pageable pageable) {
        Specification<Cinema> spec = Specification.where(GenericSpecifications.withDeleted(false));

        // Add search condition if search parameter is provided
        if (search != null && !search.trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                                    cb.or(
                                            cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"),
                                            cb.like(cb.lower(root.get("code")), "%" + search.toLowerCase() + "%")
                                    )
            );
        }

        spec = spec.and(GenericSpecifications.withStatus(status));

        Page<Cinema> cinemas = cinemaRepository.findAll(spec, pageable);
        cinemas.getContent().forEach(cinema -> {
            List<Room> activeRooms = cinema.getRooms().stream()
                                           .filter(room -> !room.isDeleted())
                                           .collect(Collectors.toList());
            cinema.setRooms(activeRooms);
        });

        return cinemas;
    }

    @Override
    public Cinema getCinemaById(Integer id) {
        return cinemaRepository.findByIdAndDeleted(id, false)
                               .orElseThrow(() -> new DataNotFoundException("Không tìm thấy rạp với id: " + id));
    }

    @Override
    @Transactional
    public Cinema createCinema(CreateCinemaRequest request) {
        String slug = slugifyService.generateSlug(request.getName());
        while (cinemaRepository.existsBySlug(slug)) {
            slug = slugifyService.generateUniqueSlug(request.getName());
        }

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
                              .slug(slug)
                              .status(request.getStatus())
                              .build();

        return cinemaRepository.save(cinema);
    }

    @Override
    @Transactional
    public Cinema updateCinema(Integer id, UpdateCinemaRequest request) {
        Cinema cinema = getCinemaById(id);

        String slug = slugifyService.generateSlug(request.getName());
        while (cinemaRepository.existsBySlug(slug)) {
            slug = slugifyService.generateUniqueSlug(request.getName());
        }

        // Update fields
        cinema.setName(request.getName());
        cinema.setAddress(request.getAddress());
        cinema.setWard(request.getWard());
        cinema.setDistrict(request.getDistrict());
        if (request.getImages() != null) {
            cinema.setImages(request.getImages());
        }
        cinema.setHotline(request.getHotline());
        cinema.setSlug(slug);
        if (request.getStatus() != null) {
            cinema.setStatus(request.getStatus());
        }

        return cinemaRepository.save(cinema);
    }

    @Override
    public void deleteCinema(Integer id) {
        Cinema cinema = getCinemaById(id);
        if (cinema.getStatus() == BaseStatus.ACTIVE) {
            throw new BadRequestException("Không thể xóa rạp đang hoạt động");
        }
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
    public List<CityResponse> getCinemaCities() {
        List<CityResponse> cities = cinemaRepository.findDistinctCities(BaseStatus.ACTIVE, false);
        return cities.stream()
                     .peek(city -> city.setName(removeCityPrefix(city.getName())))
                     .collect(Collectors.toList());
    }

    private String removeCityPrefix(String name) {
        return name.replace("Thành phố ", "").replace("Tỉnh ", "");
    }
}