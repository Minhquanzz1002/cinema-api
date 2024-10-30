package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.PromotionLine;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PromotionLineRepository extends JpaRepository<PromotionLine, Integer> {
    Optional<PromotionLine> findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndCode(LocalDate startDate, LocalDate endDate, String code);

    <T> Page<T> findAllByPromotion_IdAndDeleted(int promotionId, boolean deleted, Pageable pageable, Class<T> classType);

    Optional<PromotionLine> findByCodeAndDeleted(String code, boolean deleted);
    Optional<PromotionLine> findByIdAndDeleted(int id, boolean deleted);

}
