package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.PromotionDetail;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, Integer> {
}
