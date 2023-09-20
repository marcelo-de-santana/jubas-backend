package com.jubasbackend.dto;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.OperationTime;
import com.jubasbackend.dto.profile.ProfileDTO;

import java.util.UUID;

public record EmployeeDTO(UUID id, ProfileDTO profile, OperationTime operationTime) {
    public EmployeeDTO(Employee employee) {
        this(employee.getId(), new ProfileDTO(employee.getProfile()), employee.getOperationTime());
    }
}
