package com.jubasbackend.repository;

import com.jubasbackend.entity.User;
import com.jubasbackend.entity.UserPermission;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    List<User> findUsersByUserPermission_Id(Long userPermission_id);

}
