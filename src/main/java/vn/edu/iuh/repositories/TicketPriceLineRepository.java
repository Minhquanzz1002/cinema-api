package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.TicketPriceLine;
import vn.edu.iuh.models.enums.DayType;
import vn.edu.iuh.models.enums.SeatType;
import vn.edu.iuh.projections.v1.TicketPriceLineProjection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TicketPriceLineRepository extends JpaRepository<TicketPriceLine, Integer> {
    @Query(value = """
        SELECT tpd.price AS price, tpd.seatType as seatType
        FROM TicketPriceLine AS tpl
        JOIN TicketPriceDetail AS tpd ON tpd.ticketPriceLine.id = tpl.id
        JOIN TicketPrice AS tp ON tp.id = tpl.id
        WHERE (:date BETWEEN tp.startDate AND tp.endDate)
        AND (:time BETWEEN tpl.startTime AND tpl.endTime)
        AND array_contains(tpl.applyForDays, :type)
    """)
    List<TicketPriceLineProjection> findByDayTypeAndDateAndTime(@Param("type") String dayType, @Param("date") LocalDate date, @Param("time") LocalTime time);
}
