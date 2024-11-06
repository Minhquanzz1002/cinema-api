package vn.edu.iuh.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.models.Refund;

public class RefundSpecifications {
    public static Specification<Refund> withRefundCode(String refundCode) {
        return (root, query, cb) -> {
            if (refundCode == null || refundCode.trim().isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("code")),
                    "%" + refundCode.toLowerCase() + "%");
        };
    }

    public static Specification<Refund> withOrderCode(String orderCode) {
        return (root, query, cb) -> {
            if (orderCode == null || orderCode.trim().isEmpty()) {
                return null;
            }
            Join<Refund, Order> orderJoin = root.join("order", JoinType.INNER);
            return cb.like(cb.lower(orderJoin.get("code")),
                    "%" + orderCode.toLowerCase() + "%");
        };
    }
}
