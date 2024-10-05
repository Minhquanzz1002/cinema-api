package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.PromotionLine;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PromotionLineRepository extends JpaRepository<PromotionLine, Integer> {
    Optional<PromotionLine> findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndCode(LocalDate startDate, LocalDate endDate, String code);
}
