package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.EmployeeSpecialtyEntity;
import com.jubasbackend.domain.entity.EmployeeSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeSpecialtyRepository extends JpaRepository<EmployeeSpecialtyEntity, EmployeeSpecialtyId> {
    Optional<EmployeeSpecialtyEntity> findByEmployeeIdAndSpecialtyId(UUID employeeId, UUID specialtyId);
}
