package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.EmployeeSpecialty;
import com.jubasbackend.domain.entity.EmployeeSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeSpecialtyRepository extends JpaRepository<EmployeeSpecialty, EmployeeSpecialtyId> {
    Optional<EmployeeSpecialty> findByEmployeeIdAndSpecialtyId(UUID employeeId, UUID specialtyId);
}
