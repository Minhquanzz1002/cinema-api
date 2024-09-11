package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Director;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {
}
