package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.UserEntity;
import com.jubasbackend.domain.entity.enums.PermissionType;
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
