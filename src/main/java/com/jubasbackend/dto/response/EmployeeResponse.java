package com.jubasbackend.dto.response;

import com.jubasbackend.domain.entity.Employee;

import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        ProfileResponse profile,
        WorkingHoursResponse workingHour
) {
    public EmployeeResponse(Employee employee) {
        this(
                employee.getId(),
                new ProfileResponse(employee.getProfile()),
                new WorkingHoursResponse(employee.getWorkingHours())
        );
    }
}
