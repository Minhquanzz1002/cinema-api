package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.OrderDetail;
import vn.edu.iuh.models.Seat;
import vn.edu.iuh.models.ShowTime;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    List<OrderDetail> findAllByOrder_ShowTimeAndSeatIn(ShowTime showTime, List<Seat> seats);
}
