package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.EmployeeEntity;

import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        ProfileResponse profile,
        WorkingHoursResponse workingHour
) {
    public EmployeeResponse(EmployeeEntity employee) {
        this(
                employee.getId(),
                new ProfileResponse(employee.getProfile()),
                new WorkingHoursResponse(employee.getWorkingHours())
        );
    }
}
