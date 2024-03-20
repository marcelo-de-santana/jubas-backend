package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.AppointmentEntity;
import com.jubasbackend.domain.entity.EmployeeEntity;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.jubasbackend.utils.AppointmentsUtils.filterSchedulesAfterTime;
import static com.jubasbackend.utils.AppointmentsUtils.filterAvailableTimeSlots;

public record ScheduleResponse(UUID employeeId, String employeeName, List<ScheduleTimeResponse> workingHours) {
    public static ScheduleResponse createFromEmployeeEssentials(EmployeeEntity employee) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), employee.getWorkingHour().getOpeningHours());
    }

    public static ScheduleResponse constructFromEmployeeWithAvailableTimes(EmployeeEntity employee, List<AppointmentEntity> appointments) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), employee.getWorkingHour().getAvailableTimes(appointments));
    }

    public static ScheduleResponse createFromFilteredTimes(EmployeeEntity employee, LocalTime filterTime) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), filterSchedulesAfterTime(employee.getWorkingHour().getOpeningHours(), filterTime));
    }

    public static ScheduleResponse constructFromAvailableTimesAfterSpecificTime(EmployeeEntity employee, List<AppointmentEntity> appointments, LocalTime filterTime) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), filterSchedulesAfterTime(employee.getWorkingHour().getAvailableTimes(appointments), filterTime));
    }

    public static ScheduleResponse createFromAvailableTimesWithinBounds(EmployeeEntity employee) {
        var availableTimes = filterAvailableTimeSlots(employee.getWorkingHour().getOpeningHours());
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), availableTimes);
    }

    public static ScheduleResponse constructFromAvailableSchedulesWithinRange(EmployeeEntity employee, List<AppointmentEntity> appointments) {
        var availableTimes = filterAvailableTimeSlots(employee.getWorkingHour().getAvailableTimes(appointments));
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), availableTimes);
    }

    public static ScheduleResponse createFromPossibleTimesForSpecialty(EmployeeEntity employee, List<AppointmentEntity> appointments, UUID specialtyId) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(),  employee.getPossibleTimes(specialtyId, appointments));
    }

    public static ScheduleResponse constructFromAvailableTimesForSpecificSpecialty(EmployeeEntity employee, List<AppointmentEntity> appointments, UUID specialtyId, LocalTime filterTime) {
        var availableTimes = filterSchedulesAfterTime(employee.getPossibleTimes(specialtyId, appointments), filterTime);
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), availableTimes);
    }

}
