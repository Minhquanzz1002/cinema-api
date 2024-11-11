package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    <T> List<T> findProjectionByPhoneContainingAndDeletedAndRole_Name(String phone, boolean deleted, String roleName, Class<T> classType);

    Optional<User> findByIdAndDeletedFalseAndRole_Name(UUID id, String roleName);

    Page<User> findByDeletedFalseAndRole_Name(String roleName, Pageable pageable);

    Page<User> findByPhoneContainingOrNameContainingAndDeletedFalseAndRole_Name(
            String phone, String name, String roleName, Pageable pageable);

    Optional<User> findByCode(String code);


    Optional<User> findTopByOrderByCodeDesc();
}
