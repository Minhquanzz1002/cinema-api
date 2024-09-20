package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Room;
import vn.edu.iuh.models.RoomLayout;

import java.util.Optional;

@Repository
public interface RoomLayoutRepository extends JpaRepository<RoomLayout, Integer> {
    <T> Optional<T> findByRoom(Room room, Class<T> classType);
}
