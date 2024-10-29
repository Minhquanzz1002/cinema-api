package vn.edu.iuh.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.TicketPrice;

import java.time.LocalDate;

public class TicketPriceSpecification {
    public static Specification<TicketPrice> withName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<TicketPrice> betweenDates(LocalDate startDate, LocalDate endDate) {
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
