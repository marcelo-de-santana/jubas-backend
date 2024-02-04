package com.jubasbackend.core.employee_specialty;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeSpecialtyRepository extends JpaRepository<EmployeeSpecialtyEntity, EmployeeSpecialtyId> {
    Optional<EmployeeSpecialtyEntity> findByEmployeeIdAndSpecialtyId(UUID employeeId, UUID specialtyId);
}
