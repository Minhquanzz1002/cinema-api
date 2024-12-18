package vn.edu.iuh.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    boolean existsByEmailAndDeleted(String email, boolean deleted);

    boolean existsByPhoneAndDeleted(String phone, boolean deleted);

    boolean existsByIdAndDeleted(UUID id, boolean deleted);

    <T> List<T> findProjectionByPhoneContainingAndDeletedAndRole_Name(String phone, boolean deleted, String roleName, Class<T> classType);

    Optional<User> findByIdAndDeletedFalseAndRole_Name(UUID id, String roleName);
    Optional<User> findByIdAndDeletedAndRole_Name(UUID id, boolean deleted, String roleName);
    Page<User> findByPhoneContainingOrNameContainingAndDeletedFalseAndRole_Name(
            String phone, String name, String roleName, Pageable pageable);

    Optional<User> findByCode(String code);


    Optional<User> findTopByCodeStartingWithOrderByCodeDesc(String prefix);

    Optional<User> findByIdAndDeleted(UUID id, boolean deleted);

    int countAllByCodeStartingWithAndStatusAndDeleted(String code, UserStatus status, boolean deleted);
}
