package com.jubasbackend.core.appointment.utils;

import com.jubasbackend.core.appointment.AppointmentEntity;
import com.jubasbackend.core.appointment.dto.ScheduleResponse;
import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.employee.EmployeeRepository;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.jubasbackend.core.appointment.dto.ScheduleResponse.*;

public class AppointmentsUtils {

    public static LocalDateTime getCurrentOrFutureDate(LocalDate date) {
        return date.equals(LocalDate.now()) ? LocalDateTime.now() : date.atStartOfDay();
    }

    public static void validateAppointment(List<AppointmentEntity> registeredAppointments, AppointmentEntity requestAppointment) {
        registeredAppointments.forEach(existingAppointment -> existingAppointment.validate(requestAppointment));
    }

    public static List<ScheduleResponse> getPossibleTimesBySpecialty(UUID specialtyId,
                                                                     List<EmployeeEntity> employees,
                                                                     List<AppointmentEntity> appointments,
                                                                     LocalTime filterTime) {
        var possibleTimes = employees.stream()
                .filter(employee -> employee.makesSpecialty(specialtyId))
                .map(filteredEmployee -> fromEmployeeWithAppointments(filteredEmployee, appointments, specialtyId, filterTime))
                .toList();

        if (possibleTimes.isEmpty())
            throw new NoSuchElementException("No available time slots for the service.");

        return possibleTimes;
    }

    public static List<ScheduleTimeResponse> filterSchedulesAfterTime(List<ScheduleTimeResponse> schedules, LocalTime cutoffTime) {
        return schedules.stream()
                .filter(schedule -> !schedule.time().isBefore(cutoffTime))
                .toList();
    }

    public static List<EmployeeEntity> getAvailableEmployees(EmployeeRepository employeeRepository) {
        var availableEmployees = employeeRepository.findAllByActiveProfile(true);
        if (availableEmployees.isEmpty())
            throw new NoSuchElementException("No employees.");

        return availableEmployees;
    }

    public static ScheduleResponse toScheduleResponse(EmployeeEntity employee, List<AppointmentEntity> appointments, LocalTime filterTime, boolean toFilter) {
        if (toFilter) {
            return appointments.isEmpty() ? fromEmployeeWithFilterTime(employee, filterTime) :
                    fromEmployeeWithAppointmentsAndFilterTime(employee, appointments, filterTime);
        }
        return appointments.isEmpty() ? fromEmployee(employee) : fromEmployeeWithAppointments(employee, appointments);

    }
}
