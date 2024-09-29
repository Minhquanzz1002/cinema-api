package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.PromotionLine;

@Repository
public interface PromotionLineRepository extends JpaRepository<PromotionLine, Integer> {
}
