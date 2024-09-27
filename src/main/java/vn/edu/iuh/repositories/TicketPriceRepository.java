package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.enums.BaseStatus;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TicketPriceRepository extends JpaRepository<TicketPrice, Integer> {
    @Query("SELECT tp FROM TicketPrice tp " +
            "WHERE tp.startDate <= :date AND tp.endDate >= :date " +
            "AND tp.status = :status " +
            "AND tp.deleted = false")
    Optional<TicketPrice> findByStatusAndDate(@Param("date") LocalDate date, @Param("status") BaseStatus status);
}
