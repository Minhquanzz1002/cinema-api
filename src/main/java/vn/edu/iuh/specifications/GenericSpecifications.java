package vn.edu.iuh.specifications;

import org.springframework.data.jpa.domain.Specification;

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
}
