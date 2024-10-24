package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Actor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    <T> List<T> findAllByStatusAndDeleted(BaseStatus status, boolean deleted, Class<T> classType);

    <T> Page<T> findAllByStatusAndDeletedAndCodeContainingIgnoreCaseAndNameContainingIgnoreCase(BaseStatus status, boolean deleted, String code, String name, Pageable pageable, Class<T> classType);

    Optional<Actor> findTopByOrderByCodeDesc();
    Optional<Actor> findByCode(String code);
}
