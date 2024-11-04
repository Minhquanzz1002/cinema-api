package vn.edu.iuh.specifications;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.iuh.models.ProductPrice;
import vn.edu.iuh.models.enums.BaseStatus;

import java.util.List;

public class ProductPriceSpecifications {
    public static Specification<ProductPrice> withFilters(String code, BaseStatus status, boolean deleted) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new java.util.ArrayList<>(List.of(
                    criteriaBuilder.equal(root.get("product").get("code"), code),
                    criteriaBuilder.equal(root.get("deleted"), deleted)
            ));

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ProductPrice> withProductCodeOrName(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            var product = root.join("product", JoinType.INNER);

            String likePattern = "%" + searchTerm.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(product.get("code")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(product.get("name")), likePattern)
            );
        };
    }
}
