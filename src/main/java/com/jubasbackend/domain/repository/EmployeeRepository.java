package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.EmployeeEntity;
import com.jubasbackend.domain.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {
     Optional<EmployeeEntity> findEmployeeByProfileId(UUID id);

     boolean existsByProfile(ProfileEntity profile);
}
