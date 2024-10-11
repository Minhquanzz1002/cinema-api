package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Producer;
import vn.edu.iuh.models.enums.BaseStatus;

import java.util.List;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Integer> {
    <T> List<T> findAllByStatusAndDeleted(BaseStatus status, boolean deleted, Class<T> classType);
}
