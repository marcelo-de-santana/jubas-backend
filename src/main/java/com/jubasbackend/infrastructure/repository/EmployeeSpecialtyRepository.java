package com.jubasbackend.infrastructure.repository;

import com.jubasbackend.infrastructure.entity.EmployeeSpecialtyEntity;
import com.jubasbackend.infrastructure.entity.EmployeeSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeSpecialtyRepository extends JpaRepository<EmployeeSpecialtyEntity, EmployeeSpecialtyId> {
}
