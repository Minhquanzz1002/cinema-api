package vn.edu.iuh.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.UserStatus;

import static vn.edu.iuh.constant.SecurityConstant.ROLE_CLIENT;

public class UserSpecification {
    public static Specification<User> hasRole(String roleName) {
        return (root, query, cb) -> (roleName == null || roleName.isEmpty()) ? null : cb.equal(root.get("role").get("name"), roleName);
    }

    public static Specification<User> hasStatus(UserStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<User> excludeClientRole() {
        return (root, query, cb) -> cb.notEqual(root.get("role").get("name"), ROLE_CLIENT);
    }

    public static Specification<User> hasSearchKey(String search) {
        return (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return null;
            }
            String searchLike = "%" + search.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("phone")), searchLike),
                    cb.like(cb.lower(root.get("name")), searchLike),
                    cb.like(cb.lower(root.get("code")), searchLike)
            );
        };
    }
}
