package vn.edu.iuh.specifications;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.Promotion;
import vn.edu.iuh.models.PromotionLine;
import vn.edu.iuh.models.enums.BaseStatus;

public class PromotionLineSpecification {
    public static Specification<PromotionLine> withCode(String code) {
        return (root, query, cb) -> {
            if (code == null || code.isBlank()) {
                return cb.conjunction();
            }
            String codePattern = "%" + code + "%";
            return cb.equal(root.get("code"), codePattern);
        };
    }

    public static Specification<PromotionLine> withActivePromotion() {
        return (root, query, cb) -> {
            Join<PromotionLine, Promotion> promotionJoin = root.join("promotion");
            return cb.equal(promotionJoin.get("status"), BaseStatus.ACTIVE);
        };
    }
}
