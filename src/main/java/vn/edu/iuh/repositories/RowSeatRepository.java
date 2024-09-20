package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.RowSeat;

@Repository
public interface RowSeatRepository extends JpaRepository<RowSeat, Integer> {
}
