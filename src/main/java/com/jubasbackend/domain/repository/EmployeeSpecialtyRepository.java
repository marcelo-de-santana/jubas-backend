package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.EmployeeSpecialty;
import com.jubasbackend.domain.entity.EmployeeSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeeSpecialtyRepository extends JpaRepository<EmployeeSpecialty, EmployeeSpecialtyId> {
    List<EmployeeSpecialty> findAllByEmployeeId(UUID employeeId);
}
