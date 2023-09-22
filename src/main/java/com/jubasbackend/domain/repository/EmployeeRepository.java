package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
     Employee findEmployeeByProfileId(UUID id);
}
