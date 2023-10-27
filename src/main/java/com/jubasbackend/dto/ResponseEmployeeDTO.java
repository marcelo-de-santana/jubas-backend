package com.jubasbackend.dto;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.WorkingHours;
import com.jubasbackend.dto.ResponseProfileDTO;

import java.util.UUID;

public record ResponseEmployeeDTO(
        UUID id,
        ResponseProfileDTO profile,
        WorkingHours workingHours
) {
    public ResponseEmployeeDTO(Employee employee) {
        this(
                employee.getId(),
                new ResponseProfileDTO(employee.getProfile()),
                employee.getWorkingHours()
        );
    }
}
