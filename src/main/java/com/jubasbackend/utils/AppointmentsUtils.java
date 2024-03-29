package com.jubasbackend.utils;

import com.jubasbackend.controller.response.ScheduleResponse;
import com.jubasbackend.controller.response.ScheduleTimeResponse;
import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.repository.EmployeeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.jubasbackend.controller.response.ScheduleResponse.*;

public class AppointmentsUtils {

    public static LocalDateTime getDateTimeForAppointment(LocalDate date) {
        return date.equals(LocalDate.now()) ? LocalDateTime.now() : date.atStartOfDay();
    }

    public static void validateAppointmentOverlap(List<Appointment> registeredAppointments,
                                                  Appointment requestAppointment) {
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

    public static List<Employee> findAvailableEmployees(EmployeeRepository employeeRepository) {
        var availableEmployees = employeeRepository.findAllByActiveProfile(true);
        if (availableEmployees.isEmpty())
            throw new NoSuchElementException("No employees.");

        return availableEmployees;
    }

    public static List<ScheduleResponse> generateScheduleResponses(LocalDate requestDate,
                                                                   UUID specialtyId,
                                                                   boolean toFilter,
                                                                   LocalTime filterTime,
                                                                   List<Employee> availableEmployees,
                                                                   List<Appointment> appointments) {
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
                                                                          List<Employee> availableEmployees,
                                                                          List<Appointment> appointments) {
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

    private static ScheduleResponse getTimesFilteredByTheCurrentTime(Employee employee,
                                                                     List<Appointment> appointments,
                                                                     LocalTime filterTime) {
        return appointments.isEmpty() ? ScheduleResponse.createFromFilteredTimes(employee, filterTime) :
                constructFromAvailableTimesAfterSpecificTime(employee, appointments, filterTime);
    }

    private static ScheduleResponse toScheduleResponse(Employee employee,
                                                       List<Appointment> appointments,
                                                       boolean filterByDate) {
        if (filterByDate) {
            return appointments.isEmpty() ? createFromAvailableTimesWithinBounds(employee) :
                    constructFromAvailableSchedulesWithinRange(employee, appointments);
        }
        return appointments.isEmpty() ? createFromEmployeeEssentials(employee) :
                constructFromEmployeeWithAvailableTimes(employee, appointments);
    }
}
