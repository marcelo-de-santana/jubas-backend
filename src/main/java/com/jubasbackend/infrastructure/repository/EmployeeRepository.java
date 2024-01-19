package com.jubasbackend.infrastructure.repository;

import com.jubasbackend.infrastructure.entity.EmployeeEntity;
import com.jubasbackend.infrastructure.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {
     boolean existsByProfile(ProfileEntity profile);
}
