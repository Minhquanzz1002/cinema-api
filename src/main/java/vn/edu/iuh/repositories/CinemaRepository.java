package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Cinema;

import java.util.List;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
    <T> List<T> findAllProjectionBy(Class<T> classType);
    <T> List<T> findAllProjectionByDeleted(boolean deleted, Class<T> classType);
}
