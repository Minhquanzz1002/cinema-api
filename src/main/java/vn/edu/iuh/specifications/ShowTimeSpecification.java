package vn.edu.iuh.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.ShowTime;

import java.time.LocalDate;

public class ShowTimeSpecification {
    public static Specification<ShowTime> withCinema(Integer cinemaId) {
        return (root, query, criteriaBuilder) -> {
            if (cinemaId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("cinema").get("id"), cinemaId);
        };
    }

    public static Specification<ShowTime> withMovie(Integer movieId) {
        return (root, query, criteriaBuilder) -> {
            if (movieId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("movie").get("id"), movieId);
        };
    }

    public static Specification<ShowTime> onDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            LocalDate searchDate = date != null ? date : LocalDate.now();
            return criteriaBuilder.equal(root.get("startDate"), searchDate);
        };
    }
}
