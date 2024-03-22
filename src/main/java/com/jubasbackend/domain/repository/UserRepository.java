package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.enums.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    List<User> findAllByPermission(PermissionType permissionId);

    Optional<User> findByEmail(String email);


}
