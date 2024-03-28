package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.enums.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    Optional<Profile> findByCpfAndUserEmail(String cpf, String email);

    List<Profile> findAllByUserPermission(PermissionType permission);
}
