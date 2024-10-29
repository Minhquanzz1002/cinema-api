package vn.edu.iuh.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.Promotion;

public class PromotionSpecification {

    public static Specification<Promotion> withNameOrCode(String value) {
        return (root, query, cb) -> {
            if (value == null || value.trim().isEmpty()) {
                return cb.conjunction();
            }
            String searchLower = "%" + value.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), searchLower),
                    cb.like(cb.lower(root.get("code")), searchLower)
            );
        };
    }
}
