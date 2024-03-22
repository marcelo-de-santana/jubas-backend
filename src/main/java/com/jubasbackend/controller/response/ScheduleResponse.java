package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.entity.Employee;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.jubasbackend.utils.AppointmentsUtils.filterSchedulesAfterTime;
import static com.jubasbackend.utils.AppointmentsUtils.filterAvailableTimeSlots;

public record ScheduleResponse(UUID employeeId, String employeeName, List<ScheduleTimeResponse> workingHours) {
    public static ScheduleResponse createFromEmployeeEssentials(Employee employee) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), employee.getWorkingHour().getOpeningHours());
    }

    public static ScheduleResponse constructFromEmployeeWithAvailableTimes(Employee employee, List<Appointment> appointments) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), employee.getWorkingHour().getAvailableTimes(appointments));
    }

    public static ScheduleResponse createFromFilteredTimes(Employee employee, LocalTime filterTime) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), filterSchedulesAfterTime(employee.getWorkingHour().getOpeningHours(), filterTime));
    }

    public static ScheduleResponse constructFromAvailableTimesAfterSpecificTime(Employee employee, List<Appointment> appointments, LocalTime filterTime) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), filterSchedulesAfterTime(employee.getWorkingHour().getAvailableTimes(appointments), filterTime));
    }

    public static ScheduleResponse createFromAvailableTimesWithinBounds(Employee employee) {
        var availableTimes = filterAvailableTimeSlots(employee.getWorkingHour().getOpeningHours());
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), availableTimes);
    }

    public static ScheduleResponse constructFromAvailableSchedulesWithinRange(Employee employee, List<Appointment> appointments) {
        var availableTimes = filterAvailableTimeSlots(employee.getWorkingHour().getAvailableTimes(appointments));
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), availableTimes);
    }

    public static ScheduleResponse createFromPossibleTimesForSpecialty(Employee employee, List<Appointment> appointments, UUID specialtyId) {
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(),  employee.getPossibleTimes(specialtyId, appointments));
    }

    public static ScheduleResponse constructFromAvailableTimesForSpecificSpecialty(Employee employee, List<Appointment> appointments, UUID specialtyId, LocalTime filterTime) {
        var availableTimes = filterSchedulesAfterTime(employee.getPossibleTimes(specialtyId, appointments), filterTime);
        return new ScheduleResponse(employee.getId(), employee.getProfile().getName(), availableTimes);
    }

}
