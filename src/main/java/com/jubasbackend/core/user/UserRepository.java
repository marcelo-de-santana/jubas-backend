package com.jubasbackend.core.user;

import com.jubasbackend.core.user.enums.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    List<UserEntity> findAllByPermission(PermissionType permissionId);

    Optional<UserEntity> findByEmail(String email);


}
