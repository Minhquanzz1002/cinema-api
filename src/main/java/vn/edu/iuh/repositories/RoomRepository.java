package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Room;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    <T> List<T> findAllProjectionByDeletedAndCinema_Id(boolean deleted, int cinemaId,Class<T> classType);

    Optional<Room> findByIdAndDeleted(int id, boolean deleted);
}
