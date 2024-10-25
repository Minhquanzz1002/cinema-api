package vn.edu.iuh.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.MovieStatus;

public class MovieSpecification {
    public static Specification<Movie> withTitleOrCode(String search) {
        return (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }
            String searchLower = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), searchLower),
                    cb.like(cb.lower(root.get("code")), searchLower)
            );
        };
    }
    public static Specification<Movie> withCountry(String country) {
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

    public static Specification<Movie> withAgeRating(AgeRating ageRating) {
        return (root, query, cb) -> {
            if (ageRating == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("ageRating"), ageRating);
        };
    }

    public static Specification<Movie> withStatus(MovieStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Movie> withDeleted(boolean deleted) {
        return (root, query, cb) -> {
            if (deleted) {
                return cb.isTrue(root.get("deleted"));
            } else {
                return cb.isFalse(root.get("deleted"));
            }
        };
    }
}
