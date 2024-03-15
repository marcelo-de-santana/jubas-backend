package com.jubasbackend.core.appointment.dto;

import com.jubasbackend.core.appointment.AppointmentEntity;
import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.jubasbackend.core.appointment.utils.AppointmentsUtils.filterSchedulesAfterTime;

public record ScheduleResponse(UUID employeeId, String employeeName, List<ScheduleTimeResponse> workingHours) {
    public static ScheduleResponse fromEmployee(EmployeeEntity employee) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), employee.getWorkingHour().getOpeningHours());
    }

    public static ScheduleResponse fromEmployeeWithAppointments(EmployeeEntity employee, List<AppointmentEntity> appointments) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), employee.getWorkingHour().getAvailableTimes(appointments));
    }

    public static ScheduleResponse fromEmployeeWithFilterTime(EmployeeEntity employee, LocalTime filterTime) {
        var availableTimes = filterSchedulesAfterTime(employee.getWorkingHour().getOpeningHours(), filterTime);
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), availableTimes);
    }

    public static ScheduleResponse fromEmployeeWithAppointmentsAndFilterTime(EmployeeEntity employee, List<AppointmentEntity> appointments, LocalTime filterTime) {
        var availableTimes = filterSchedulesAfterTime(employee.getWorkingHour().getAvailableTimes(appointments), filterTime);
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), availableTimes);
    }

    public static ScheduleResponse fromEmployeeWithAppointments(EmployeeEntity employee, List<AppointmentEntity> appointments, UUID specialtyId, LocalTime filterTime) {
        var availableTimes = filterSchedulesAfterTime(employee.getPossibleTimes(specialtyId, appointments), filterTime);
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), availableTimes);
    }

}
