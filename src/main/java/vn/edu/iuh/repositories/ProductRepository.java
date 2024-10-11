package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Product;
import vn.edu.iuh.models.enums.ProductStatus;
import vn.edu.iuh.projections.admin.v1.BaseProductWithPriceProjection;
import vn.edu.iuh.projections.v1.ProductProjection;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT p.id as id, p.code as code, p.name as name, p.description as description, p.image as image, p.status as status, pp.price as price FROM Product p LEFT JOIN p.productPrices pp WHERE pp.deleted = false AND pp.status = 'ACTIVE' AND pp.startDate <= CURRENT_DATE AND pp.endDate >= CURRENT_DATE AND p.deleted = :deleted AND p.status = :status")
    List<ProductProjection> findAllWithPrice(@Param("status") ProductStatus status, @Param("deleted") boolean deleted);

    @Query(value = """
                SELECT p.id as id, p.code as code, p.name as name, p.description as description, p.image as image, p.status as status, pp.price as price
                FROM Product p LEFT JOIN p.productPrices pp\s
                WHERE pp.deleted = false\s
                    AND pp.status = 'ACTIVE'\s
                    AND pp.startDate <= CURRENT_DATE\s
                    AND pp.endDate >= CURRENT_DATE\s
                    AND p.deleted = :deleted\s
                    AND p.status = :status
                    AND p.id IN :ids
            """)
    List<ProductProjection> findAllWithPriceByIds(@Param("status") ProductStatus status, @Param("deleted") boolean deleted, @Param("ids") List<Integer> ids);

    @Query(value = """
                SELECT p.id as id, p.code as code, p.name as name, p.description as description, p.image as image, p.status as status, pp.price as price
                FROM Product p LEFT JOIN p.productPrices pp\s
                WHERE pp.deleted = false\s
                    AND pp.status = 'ACTIVE'\s
                    AND pp.startDate <= CURRENT_DATE\s
                    AND pp.endDate >= CURRENT_DATE\s
                    AND p.deleted = :deleted\s
                    AND p.status = :status
                    AND p.id IN :id
            """)
    ProductProjection findWithPriceById(@Param("status") ProductStatus status, @Param("deleted") boolean deleted, @Param("id") int id);

    @Query(value = """
                SELECT p.id as id, p.code as code, p.name as name, p.description as description, p.image as image, p.status as status, pp.price as price
                FROM Product p LEFT JOIN p.productPrices pp\s
                WHERE pp.deleted = false\s
                    AND pp.status = 'ACTIVE'\s
                    AND pp.startDate <= CURRENT_DATE\s
                    AND pp.endDate >= CURRENT_DATE\s
                    AND p.deleted = :deleted\s
                    AND p.status = :status
            """,
            countQuery = """
                            SELECT COUNT(DISTINCT p.id)
                            FROM Product p\s
                            LEFT JOIN p.productPrices pp\s
                            WHERE pp.deleted = false\s
                                AND pp.status = 'ACTIVE'\s
                                AND pp.startDate <= CURRENT_DATE\s
                                AND pp.endDate >= CURRENT_DATE\s
                                AND p.deleted = :deleted\s
                                AND p.status = :status
                    """)
    Page<BaseProductWithPriceProjection> findWithPriceByStatusAndDeleted(@Param("status") ProductStatus status,
                                                                         @Param("deleted") boolean deleted,
                                                                         Pageable pageable);

    <T> Page<T> findAllByStatusAndDeleted(ProductStatus status, boolean deleted, Pageable pageable, Class<T> classType);
}
