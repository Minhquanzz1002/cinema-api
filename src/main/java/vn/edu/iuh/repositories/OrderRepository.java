package vn.edu.iuh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.Order;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    <T> List<T> findAllByUserAndStatus(User user, OrderStatus status, Class<T> classType);

    <T> Optional<T> findById(UUID id, Class<T> classType);

    Optional<Order> findByIdAndUser(UUID id, User user);

    void deleteByIdAndUser(UUID uuid, User user);
}
