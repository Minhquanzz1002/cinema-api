package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.dto.res.CityResponseDTO;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.enums.BaseStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
    <T> List<T> findAllProjectionBy(Class<T> classType);
    <T> List<T> findAllProjectionByDeleted(boolean deleted, Class<T> classType);

    Optional<Cinema> findByIdAndDeleted(int id, boolean deleted);
    Optional<Cinema> findTopByOrderByCodeDesc();

    Page<Cinema> findAll(Specification<Cinema> spec, Pageable pageable);

    @Query("SELECT DISTINCT new vn.edu.iuh.dto.res.CityResponseDTO(CAST(c.cityCode AS INTEGER) , c.city) FROM Cinema c " +
            "WHERE c.status = :status " +
            "AND c.deleted = :deleted " +
            "ORDER BY c.city")
    List<CityResponseDTO> findDistinctCities(@Param("status") BaseStatus status, @Param("deleted") boolean deleted);

    int countAllByStatusAndDeleted(BaseStatus status, boolean deleted);

    List<Cinema> findAllByStatusAndDeleted(BaseStatus status, boolean deleted);
}
