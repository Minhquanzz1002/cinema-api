package vn.edu.iuh.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.Actor;
import vn.edu.iuh.models.enums.BaseStatus;

public class ActorSpecification {

    public static Specification<Actor> withNameOrCode(String value) {
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

    public static Specification<Actor> withStatus(BaseStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Actor> withCountry(String country) {
        return (root, query, cb) -> {
            if (country == null || country.trim().isEmpty()) {
                return null;
            }
            return cb.like(
                    cb.lower(root.get("country")),
                    "%" + country.toLowerCase() + "%"
            );
        };
    }
}
