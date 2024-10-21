package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.ProductPrice;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Integer> {
    Page<ProductPrice> findAllByProduct_CodeAndDeleted(String code, boolean deleted,Pageable pageable);
}
