package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Cinema;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {
}
