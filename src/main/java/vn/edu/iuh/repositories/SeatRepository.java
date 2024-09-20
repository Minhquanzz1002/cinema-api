package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
}
