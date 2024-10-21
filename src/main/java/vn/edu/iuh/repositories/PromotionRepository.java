package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Promotion;
import vn.edu.iuh.models.enums.BaseStatus;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    <T> List<T> findAllByStatusAndDeleted(BaseStatus status, boolean deleted, Class<T> classType);
    <T> Page<T> findAllByStatusAndDeletedAndCodeContainingIgnoreCaseAndNameContainingIgnoreCase(BaseStatus status, boolean deleted, String code, String name, Pageable pageable, Class<T> classType);
    <T> Page<T> findAllByDeletedAndCodeContainingIgnoreCaseAndNameContainingIgnoreCase(boolean deleted, String code, String name, Pageable pageable, Class<T> classType);
    @Query("SELECT p FROM Promotion p LEFT JOIN FETCH p.promotionLines pl " +
           "WHERE p.deleted = :deleted " +
           "AND LOWER(p.code) LIKE LOWER(CONCAT('%', :code, '%')) " +
           "AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "AND pl.deleted = false")
    <T> Page<T> findAllByDeletedAndCodeContainingIgnoreCaseAndNameContainingIgnoreCaseWithActivePromotionLines(
            @Param("deleted") boolean deleted,
            @Param("code") String code, @Param("name") String name,
            Pageable pageable, Class<T> classType);
}
