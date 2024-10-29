package vn.edu.iuh.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.models.enums.OrderStatus;

import java.time.LocalDate;

public class OrderSpecifications {
    public static Specification<Order> withCode(String code) {
        return (root, query, cb) -> {
            if (code == null || code.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("code")), "%" + code.toLowerCase() + "%");
        };
    }

    public static Specification<Order> withStatus(OrderStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Order> withFromDate(LocalDate fromDate) {
        return (root, query, cb) -> {
            if (fromDate == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("orderDate"), fromDate);
        };
    }

    public static Specification<Order> withToDate(LocalDate toDate) {
        return (root, query, cb) -> {
            if (toDate == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("orderDate"), toDate);
        };
    }

    public static Specification<Order> withFilters(String code, OrderStatus status, LocalDate fromDate, LocalDate toDate) {
        return Specification
                .where(withCode(code))
                .and(withStatus(status))
                .and(withFromDate(fromDate))
                .and(withToDate(toDate));
    }

}
