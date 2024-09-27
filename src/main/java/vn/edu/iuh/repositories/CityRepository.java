package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.City;
import vn.edu.iuh.projections.v1.CityProjection;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    List<CityProjection> findAllProjectionBy();
}
