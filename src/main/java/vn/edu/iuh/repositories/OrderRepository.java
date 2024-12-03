package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.dto.admin.v1.res.AdminEmployeeReportResponseDTO;
import vn.edu.iuh.dto.admin.v1.res.AdminMovieReportResponseDTO;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    boolean existsByCode(String code);
    
    <T> List<T> findAllByUserAndStatusIn(User user, List<OrderStatus> statuses, Class<T> classType);

    <T> Optional<T> findById(UUID id, Class<T> classType);

    Optional<Order> findByIdAndDeleted(UUID id, boolean deleted);

    Optional<Order> findByIdAndDeletedAndStatus(UUID id, boolean deleted, OrderStatus status);

    <T> Optional<T> findByCode(String code, Class<T> classType);

    Optional<Order> findByIdAndUserAndDeleted(UUID id, User user, boolean deleted);

    Optional<Order> findByIdAndUserAndDeletedAndStatus(UUID id, User user, boolean deleted, OrderStatus status);

    void deleteByIdAndUser(UUID uuid, User user);

    <T> Page<T> findAllByCodeContainingAndStatusAndDeleted(
            String code,
            OrderStatus status,
            boolean deleted,
            Pageable pageable,
            Class<T> classType
    );

    <T> Page<T> findAllByCodeContainingAndDeleted(String code, boolean deleted, Pageable pageable, Class<T> classType);

    @Query("""
                SELECT new vn.edu.iuh.dto.admin.v1.res.AdminEmployeeReportResponseDTO(
                    u.code,
                    u.name,
                    CAST(DATE(o.orderDate) AS localdate) ,
                    CAST(COALESCE(SUM(o.totalPrice), 0) AS float),
                    CAST(COALESCE(SUM(o.totalDiscount), 0) AS float),
                    CAST(COALESCE(SUM(o.finalAmount), 0) AS float)
                ) FROM User u
                INNER JOIN Order o ON u.id = o.createdBy AND DATE(o.orderDate) BETWEEN :fromDate AND :toDate
                WHERE (u.role.name = 'ROLE_ADMIN' OR u.role.name = 'ROLE_EMPLOYEE_SALE')
                AND (:search IS NULL
                    OR TRIM(:search) = ''
                    OR LOWER(u.code) LIKE LOWER(CONCAT('%', TRIM(:search), '%'))
                    OR LOWER(u.name) LIKE LOWER(CONCAT('%', TRIM(:search), '%')))
                AND o.status = 'COMPLETED'
                GROUP BY u.name, u.code, DATE(o.orderDate)
                ORDER BY DATE(o.orderDate) ASC
            """)
    List<AdminEmployeeReportResponseDTO> getEmployeeSalesPerformanceReportData(
            LocalDate fromDate,
            LocalDate toDate,
            String search
    );

    @Query("""
                SELECT new vn.edu.iuh.dto.admin.v1.res.AdminMovieReportResponseDTO(
                    s.movie.code,
                    s.movie.title,
                    s.startDate,
                    CAST(COALESCE(COUNT(DISTINCT s.id), 0) AS int),
                    CAST(COALESCE(COUNT(od.id), 0) AS int),
                    CAST(COALESCE(SUM(od.price), 0) AS float)
                ) FROM ShowTime s
                LEFT JOIN s.orders o ON o.status = 'COMPLETED'
                LEFT JOIN o.orderDetails od ON od.type = 'TICKET'
                WHERE s.startDate BETWEEN :fromDate AND :toDate
                AND s.status = 'ACTIVE'
                AND s.deleted = false
                AND (:search IS NULL
                    OR TRIM(:search) = ''
                    OR LOWER(s.movie.code) LIKE LOWER(CONCAT('%', TRIM(:search), '%'))
                    OR LOWER(s.movie.title) LIKE LOWER(CONCAT('%', TRIM(:search), '%')))
                GROUP BY s.movie.code, s.movie.title, s.startDate
                ORDER BY s.startDate ASC
            """)
    List<AdminMovieReportResponseDTO> getMovieSalesPerformanceReportData(
            LocalDate fromDate,
            LocalDate toDate,
            String search
    );

    List<Order> findAllByStatusAndDeletedAndOrderDateBetween(OrderStatus status, boolean deleted, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Order findTopByOrderByIdDesc();
}
