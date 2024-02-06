package com.jubasbackend.core.profile;

import com.jubasbackend.core.user.enums.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {

    Optional<ProfileEntity> findByCpfAndUserEmail(String cpf, String email);

    boolean existsByUserId(UUID userId);

    List<ProfileEntity> findAllByUserPermission(PermissionType permission);
}
