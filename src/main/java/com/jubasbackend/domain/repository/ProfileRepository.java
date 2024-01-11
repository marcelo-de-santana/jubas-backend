package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {
    List<ProfileEntity> findAllByUserId(UUID id);

    List<ProfileEntity> findAllByUserPermissionId(Short id);

    Optional<ProfileEntity> findByCpfAndUserEmail(String cpf, String email);

}
