package com.jubasbackend.core.appointment.dto;

import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.specialty.dto.SpecialtyResponse;
import com.jubasbackend.core.workingHour.dto.ScheduledTimeWithoutId;

import java.util.List;
import java.util.UUID;

public record AppointmentScheduleResponse(
        UUID id,
        String name,
        boolean statusProfile,
        List<SpecialtyResponse> specialties, List<ScheduledTimeWithoutId> availableTime
) {
    public AppointmentScheduleResponse(EmployeeEntity employee, List<ScheduledTimeWithoutId> availableTimes) {
        this(
                employee.getId(),
                employee.getProfile().getName(),
                employee.getProfile().isStatusProfile(),
                employee.getSpecialties().stream().map(SpecialtyResponse::new).toList(),
                availableTimes
        );
    }
}
