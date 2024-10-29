package vn.edu.iuh.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;

public class GenericSpecifications {
    public static <T> Specification<T> withDeleted(boolean deleted) {
        return (root, query, cb) -> {
            if (deleted) {
                return cb.isTrue(root.get("deleted"));
            } else {
                return cb.isFalse(root.get("deleted"));
            }
        };
    }

    public static <T> Specification<T> withStatus(BaseStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static <T> Specification<T> betweenDates(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) {
                return cb.conjunction();
            }

            if (startDate != null && endDate != null) {
                return cb.and(
                        cb.greaterThanOrEqualTo(root.get("startDate"), startDate),
                        cb.lessThanOrEqualTo(root.get("endDate"), endDate)
                );
            }

            if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get("startDate"), startDate);
            }

            return cb.lessThanOrEqualTo(root.get("endDate"), endDate);
        };
    }
}
