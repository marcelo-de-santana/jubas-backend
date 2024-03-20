package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {
    default List<EmployeeEntity> findAllByActiveProfile(boolean isActive) {
        return findAll().stream()
                .filter(employee -> employee.getProfile().isStatusProfile() == isActive)
                .toList();
    }

    default Optional<EmployeeEntity> findByIdAndActiveProfile(UUID employeeId, boolean isActive) {
        return findById(employeeId)
                .filter(employee -> employee.getProfile().isStatusProfile() == isActive);
    }

}
