package com.jubasbackend.core.employee.dto;

import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.specialty.dto.SpecialtyResponse;
import com.jubasbackend.core.workingHour.dto.WorkingHourResponse;

import java.util.List;
import java.util.UUID;

public record EmployeeWorkingHourSpecialtiesResponse(
        UUID id,
        String name,
        boolean statusProfile,
        WorkingHourResponse workingHour,
        List<SpecialtyResponse> specialties
) {
    public EmployeeWorkingHourSpecialtiesResponse(EmployeeEntity employee) {
        this(
                employee.getId(),
                employee.getProfile().getName(),
                employee.getProfile().isStatusProfile(),
                new WorkingHourResponse(employee.getWorkingHour()),
                employee.getSpecialties().stream().map(SpecialtyResponse::new).toList()
        );
    }
}
