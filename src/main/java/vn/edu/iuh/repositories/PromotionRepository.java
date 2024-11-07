package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Promotion;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer>, JpaSpecificationExecutor<Promotion> {
    <T> List<T> findAllByStatusAndDeleted(BaseStatus status, boolean deleted, Class<T> classType);

    <T> Page<T> findAllByStatusAndDeletedAndCodeContainingIgnoreCaseAndNameContainingIgnoreCase(BaseStatus status, boolean deleted, String code, String name, Pageable pageable, Class<T> classType);

    <T> Page<T> findAllByDeletedAndCodeContainingIgnoreCaseAndNameContainingIgnoreCase(boolean deleted, String code, String name, Pageable pageable, Class<T> classType);

    <T> Optional<T> findByCodeAndDeleted(String code, boolean deleted, Class<T> classType);

    Optional<Promotion> findByIdAndDeleted(int id, boolean deleted);

    boolean existsByDeletedAndStatusAndIdNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            boolean deleted, BaseStatus status, int id, LocalDate startDate, LocalDate endDate
    );
}
