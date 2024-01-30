package com.jubasbackend.core.appointment.dto;

import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.workingHour.dto.AvailableTimeResponse;
import com.jubasbackend.core.specialty.dto.SpecialtyResponse;

import java.util.List;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        String name,
        boolean statusProfile,
        List<SpecialtyResponse> specialties,
        List<AvailableTimeResponse> availableTime
) {
    public AppointmentResponse(EmployeeEntity employee) {
        this(
                employee.getId(),
                employee.getProfile().getName(),
                employee.getProfile().isStatusProfile(),
                employee.getSpecialties().stream().map(SpecialtyResponse::new).toList(),
                employee.getWorkingHour().getOpeningHours()
        );
    }
}
