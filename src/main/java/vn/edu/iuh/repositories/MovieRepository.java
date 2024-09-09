package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
