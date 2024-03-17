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

    public static LocalDateTime getDateTimeForAppointment(LocalDate date) {
        return date.equals(LocalDate.now()) ? LocalDateTime.now() : date.atStartOfDay();
    }

    public static void validateAppointmentOverlap(List<AppointmentEntity> registeredAppointments,
                                                  AppointmentEntity requestAppointment) {
        registeredAppointments.forEach(existingAppointment -> existingAppointment.validate(requestAppointment));
    }

    public static List<ScheduleTimeResponse> filterSchedulesAfterTime(List<ScheduleTimeResponse> schedules,
                                                                      LocalTime cutoffTime) {
        return schedules.stream()
                .filter(schedule -> !schedule.time().isBefore(cutoffTime))
                .toList();
    }

    public static List<ScheduleTimeResponse> filterAvailableTimeSlots(List<ScheduleTimeResponse> schedules) {
        return schedules.stream()
                .filter(ScheduleTimeResponse::isAvailable)
                .toList();
    }

    public static List<EmployeeEntity> findAvailableEmployees(EmployeeRepository employeeRepository) {
        var availableEmployees = employeeRepository.findAllByActiveProfile(true);
        if (availableEmployees.isEmpty())
            throw new NoSuchElementException("No employees.");

        return availableEmployees;
    }

    public static List<ScheduleResponse> generateScheduleResponses(LocalDate requestDate,
                                                                   UUID specialtyId,
                                                                   boolean toFilter,
                                                                   LocalTime filterTime,
                                                                   List<EmployeeEntity> availableEmployees,
                                                                   List<AppointmentEntity> appointments) {
        if (specialtyId != null) {
            return getPossibleTimesForTheSpecialty(requestDate, specialtyId, filterTime, availableEmployees, appointments);
        }

        if (requestDate == null) {
            return availableEmployees.stream()
                    .map(employee -> getTimesFilteredByTheCurrentTime(employee, appointments, filterTime))
                    .toList();
        }

        return availableEmployees.stream()
                .map(employee -> toScheduleResponse(employee, appointments, toFilter))
                .toList();
    }

    private static List<ScheduleResponse> getPossibleTimesForTheSpecialty(LocalDate requestDate,
                                                                          UUID specialtyId,
                                                                          LocalTime filterTime,
                                                                          List<EmployeeEntity> availableEmployees,
                                                                          List<AppointmentEntity> appointments) {
        return availableEmployees.stream()
                .filter(employee -> employee.makesSpecialty(specialtyId))
                .map(filteredEmployee -> {
                    if (requestDate == null) {
                        return constructFromAvailableTimesForSpecificSpecialty(filteredEmployee, appointments, specialtyId, filterTime);
                    }
                    return createFromPossibleTimesForSpecialty(filteredEmployee, appointments, specialtyId);
                })
                .toList();
    }

    private static ScheduleResponse getTimesFilteredByTheCurrentTime(EmployeeEntity employee,
                                                                     List<AppointmentEntity> appointments,
                                                                     LocalTime filterTime) {
        return appointments.isEmpty() ? ScheduleResponse.createFromFilteredTimes(employee, filterTime) :
                constructFromAvailableTimesAfterSpecificTime(employee, appointments, filterTime);
    }

    private static ScheduleResponse toScheduleResponse(EmployeeEntity employee,
                                                       List<AppointmentEntity> appointments,
                                                       boolean filterByDate) {
        if (filterByDate) {
            return appointments.isEmpty() ? createFromAvailableTimesWithinBounds(employee) :
                    constructFromAvailableSchedulesWithinRange(employee, appointments);
        }
        return appointments.isEmpty() ? createFromEmployeeEssentials(employee) :
                constructFromEmployeeWithAvailableTimes(employee, appointments);
    }
}
