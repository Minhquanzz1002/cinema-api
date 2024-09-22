package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.GroupSeat;

@Repository
public interface GroupSeatRepository extends JpaRepository<GroupSeat, Integer> {
}
