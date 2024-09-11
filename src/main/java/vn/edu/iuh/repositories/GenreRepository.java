package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
