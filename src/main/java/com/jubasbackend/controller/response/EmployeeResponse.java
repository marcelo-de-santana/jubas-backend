package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Employee;

import java.util.List;
import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        String name,
        boolean statusProfile,
        WorkingHourResponse workingHour,
        List<SpecialtyResponse> specialties
) {
    public EmployeeResponse(Employee employee) {
        this(
                employee.getId(),
                employee.getProfile().getName(),
                employee.getProfile().isStatusProfile(),
                new WorkingHourResponse(employee.getWorkingHour()),
                employee.getSpecialties().stream().map(SpecialtyResponse::new).toList()
        );
    }
}
