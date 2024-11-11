package vn.edu.iuh.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.UserStatus;

public class UserSpecification {
    public static Specification<User> hasRole(String roleName) {
        return (root, query, cb) -> cb.equal(root.get("role").get("name"), roleName);
    }

    public static Specification<User> hasStatus(UserStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<User> hasSearchKey(String search) {
        return (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return null;
            }
            String searchLike = "%" + search.trim() + "%";
            return cb.or(
                    cb.like(root.get("phone"), searchLike),
                    cb.like(root.get("name"), searchLike)
            );
        };
    }
}
