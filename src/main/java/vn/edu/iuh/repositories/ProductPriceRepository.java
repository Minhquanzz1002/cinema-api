package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.ProductPrice;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Integer>, JpaSpecificationExecutor<ProductPrice> {
    Page<ProductPrice> findAllByProduct_CodeAndDeleted(String code, boolean deleted, Pageable pageable);

    Page<ProductPrice> findAllByProduct_CodeAndDeletedAndStatus(String code, boolean deleted, BaseStatus status, Pageable pageable);

    Optional<ProductPrice> findFirstByProduct_CodeAndDeletedAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateDesc(
            String code,
            boolean deleted,
            BaseStatus status,
            LocalDate currentDate,
            LocalDate currentDateForEnd
    );

    boolean existsByProductAndDeletedAndStatusAndIdNotAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Product product,
            boolean deleted,
            BaseStatus status,
            int id,
            LocalDate endDate,
            LocalDate startDate
    );

    Optional<ProductPrice> findByIdAndDeleted(int id, boolean deleted);
}
