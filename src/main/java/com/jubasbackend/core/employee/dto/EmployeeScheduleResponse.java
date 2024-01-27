package com.jubasbackend.core.employee.dto;

import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.workingHour.dto.AvailableTimeResponse;
import com.jubasbackend.core.specialty.dto.SpecialtyResponse;

import java.util.List;
import java.util.UUID;

public record EmployeeScheduleResponse(
        UUID id,
        String name,
        boolean statusProfile,
        List<SpecialtyResponse> specialties,
        List<AvailableTimeResponse> availableTime
) {
    public EmployeeScheduleResponse(EmployeeEntity employee) {
        this(
                employee.getId(),
                employee.getProfile().getName(),
                employee.getProfile().isStatusProfile(),
                employee.getSpecialties().stream().map(SpecialtyResponse::new).toList(),
                employee.getWorkingHour().getOpeningHours()
        );
    }
}
