package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
     Optional<Employee> findEmployeeByProfileId(UUID id);

     boolean existsByProfile(Profile profile);
}
