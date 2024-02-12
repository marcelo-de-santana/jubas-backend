package com.jubasbackend.core.appointment.dto;

import com.jubasbackend.core.appointment.AppointmentEntity;
import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse;

import java.util.List;
import java.util.UUID;

public record ScheduleResponse(UUID employeeId, String employeeName, List<? extends ScheduleTimeResponse> workingHours) {
    public ScheduleResponse(EmployeeEntity employee) {
        this(employee.getId(), employee.getProfile().getName(), employee.getWorkingHour().getOpeningHours());
    }

    public ScheduleResponse(EmployeeEntity employee, List<AppointmentEntity> appointments) {
        this(employee.getId(), employee.getProfile().getName(), employee.getWorkingHour().getAvailableTimes(appointments));
    }
}
