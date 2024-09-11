package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
}
