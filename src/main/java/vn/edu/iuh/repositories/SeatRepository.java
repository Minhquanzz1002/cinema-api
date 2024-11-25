package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Seat;
import vn.edu.iuh.models.ShowTime;
import vn.edu.iuh.models.enums.SeatStatus;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    @Query(value = "select od.seat from OrderDetail od where od.order.showTime = :showTime and od.type = 'TICKET'")
    List<Seat> findBookedSeatsByShowTimeAndType(ShowTime showTime);

    @Query(value = "select count(od.seat) from OrderDetail od where od.order.showTime = :showTime and od.type = 'TICKET'")
    long countBookedSeatsByShowTime(ShowTime showTime);

    List<Seat> findAllByIdInAndStatusAndDeleted(List<Integer> ids, SeatStatus status, boolean deleted);
}
