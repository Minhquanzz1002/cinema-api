package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Producer;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Integer> {
}
