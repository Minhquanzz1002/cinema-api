package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.models.enums.ProductType;
import vn.edu.iuh.projections.ProductProjection;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT p.id as id, p.code as code, p.name as name, p.description as description, p.image as image, p.type as type, p.status as status, pp.price as price FROM Product p LEFT JOIN p.productPrices pp WHERE pp.deleted = false AND pp.status = 'ACTIVE' AND pp.startDate <= CURRENT_DATE AND pp.endDate >= CURRENT_DATE AND p.deleted = :deleted AND p.status = :status AND p.type = :type")
    List<ProductProjection> findAllWithPrice(@Param("type") ProductType type,@Param("status") ProductStatus status, @Param("deleted") boolean deleted);
}
