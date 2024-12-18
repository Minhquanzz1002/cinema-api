package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.TicketPriceLine;
import vn.edu.iuh.models.enums.DayType;
import vn.edu.iuh.projections.v1.TicketPriceLineProjection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketPriceLineRepository extends JpaRepository<TicketPriceLine, Integer> {
    //    @Query(value = """
//        SELECT tpd.price AS price, tpd.seatType as seatType
//        FROM TicketPriceLine AS tpl
//        JOIN TicketPriceDetail AS tpd ON tpd.ticketPriceLine.id = tpl.id
//        JOIN TicketPrice AS tp ON tp.id = tpl.id
//        WHERE (:date BETWEEN tp.startDate AND tp.endDate)
//        AND (:time BETWEEN tpl.startTime AND tpl.endTime)
//        AND :type MEMBER OF tpl.applyForDays
//    """)
    @Query(value = """
                 SELECT tpd.price AS price,tpd.seat_type as seatType
                 FROM ticket_price_lines AS tpl
                     JOIN ticket_price_details AS tpd ON tpl.id = tpd.ticket_price_line_id
                     JOIN ticket_prices AS tp ON tpl.ticket_price_id = tp.id
                 WHERE (:date BETWEEN tp.start_date AND tp.end_date)
             	    AND (:time BETWEEN tpl.start_time AND tpl.end_time)
             	    AND :type = ANY(tpl.apply_for_days)
             	    AND tpl.deleted = :deleted
             	    AND tpl.status = 'ACTIVE'
             	    AND tp.status = 'ACTIVE'
            """, nativeQuery = true)
    List<TicketPriceLineProjection> findByDayTypeAndDateAndTimeAndDeleted(@Param("type") String dayType, @Param("date") LocalDate date, @Param("time") LocalTime time, @Param("deleted") boolean deleted);

    Optional<TicketPriceLine> findByIdAndDeleted(int id, boolean deleted);

    @Query(
            value = """
                    SELECT EXISTS (
                                    SELECT 1 FROM ticket_price_lines tpl\s
                                    WHERE tpl.ticket_price_id = :ticketPriceId\s
                                    AND tpl.start_time <= :endTime\s
                                    AND tpl.end_time >= :startTime
                                    AND tpl.apply_for_days && CAST(:applyForDays AS text[])
                                    AND tpl.deleted = false
                                )
                    """, nativeQuery = true
    )
    boolean hasOverlappingTicketPriceLine(
            @Param("ticketPriceId") Integer ticketPriceId,
            @Param("applyForDays") String[] applyForDays,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query(
            value = """
                    SELECT EXISTS (
                                    SELECT 1 FROM ticket_price_lines tpl\s
                                    WHERE tpl.ticket_price_id = :ticketPriceId\s
                                    AND tpl.start_time <= :endTime\s
                                    AND tpl.end_time >= :startTime
                                    AND tpl.apply_for_days && CAST(:applyForDays AS text[])
                                    AND tpl.deleted = false
                                    AND (:excludeId IS NULL OR tpl.id != :excludeId)
                                )
                    """, nativeQuery = true
    )
    boolean hasOverlappingTicketPriceLine(
            @Param("ticketPriceId") Integer ticketPriceId,
            @Param("applyForDays") String[] applyForDays,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") Integer excludeId
    );
}
