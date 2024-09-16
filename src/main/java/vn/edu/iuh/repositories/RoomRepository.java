package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}