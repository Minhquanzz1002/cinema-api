package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.PromotionLine;
import vn.edu.iuh.models.enums.PromotionLineType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionLineRepository extends JpaRepository<PromotionLine, Integer> {
    Optional<PromotionLine> findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndCode(LocalDate startDate, LocalDate endDate, String code);

    <T> Page<T> findAllByPromotion_IdAndDeleted(int promotionId, boolean deleted, Pageable pageable, Class<T> classType);

    Optional<PromotionLine> findByCodeAndDeleted(String code, boolean deleted);
    Optional<PromotionLine> findByIdAndDeleted(int id, boolean deleted);

    @Query("""
        SELECT pl FROM PromotionLine pl\s
        JOIN FETCH pl.promotion p
        WHERE pl.status = 'ACTIVE'\s
        AND pl.deleted = false
        AND p.deleted = false
        AND p.status = 'ACTIVE'
        AND :currentDate BETWEEN pl.startDate AND pl.endDate
        AND :currentDate BETWEEN p.startDate AND p.endDate
    """)
    List<PromotionLine> findActivePromotionLine(@Param("currentDate") LocalDate currentDate);

    @Query("""
        SELECT CASE WHEN COUNT(pl) > 0 THEN true ELSE false END
        FROM PromotionLine pl
        WHERE pl.id != :promotionLineId
        AND pl.type = :type
        AND pl.deleted = false
        AND NOT (
            pl.endDate < :startDate\s
            OR pl.startDate > :endDate
        )
    """)
    boolean existsOverlappingPromotionLine(
            @Param("promotionLineId") int promotionLineId,
            @Param("type") PromotionLineType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
