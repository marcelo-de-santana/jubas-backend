package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.EmployeeEntity;

import java.util.List;
import java.util.UUID;

public record EmployeeProfileWorkingHourSpecialtiesResponse(
        UUID id,
        String name,
        boolean status,
        WorkingHourResponse workingHour,
        List<SpecialtyResponse> specialties
) {
    public EmployeeProfileWorkingHourSpecialtiesResponse(EmployeeEntity employee) {
        this(
                employee.getId(),
                employee.getProfile().getName(),
                employee.getProfile().isStatusProfile(),
                new WorkingHourResponse(employee.getWorkingHour()),
                employee.getSpecialties().stream().map(SpecialtyResponse::new).toList()
        );
    }
}
