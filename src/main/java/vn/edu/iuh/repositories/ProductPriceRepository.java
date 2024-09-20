package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.ProductPrice;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Integer> {
}
