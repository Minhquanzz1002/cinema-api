package vn.edu.iuh.specifications;

import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.ShowTime;

import java.time.LocalDate;

public class ShowTimeSpecification {
    public static Specification<ShowTime> withCinema(int cinemaId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cinema").get("id"), cinemaId);
    }

    public static Specification<ShowTime> onDate(LocalDate date) {
        return (root, query, criteriaBuilder) -> {
            LocalDate searchDate = date != null ? date : LocalDate.now();
            return criteriaBuilder.equal(root.get("startDate"), searchDate);
        };
    }
}
