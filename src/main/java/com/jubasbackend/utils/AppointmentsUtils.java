package com.jubasbackend.utils;

import com.jubasbackend.controller.response.EmployeeScheduleTimeResponse;
import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.jubasbackend.controller.response.EmployeeScheduleTimeResponse.createWithFilteredAvailableTimes;
import static com.jubasbackend.controller.response.EmployeeScheduleTimeResponse.createWithFilteredPossibleTimesForSpecialty;

public class AppointmentsUtils {

    private AppointmentsUtils() {
    }

    public static LocalDateTime getDateTimeForAppointment(LocalDate date) {
        return date.equals(LocalDate.now()) ? LocalDateTime.now() : date.atStartOfDay();
    }

    public static void validateAppointmentOverlap(List<Appointment> registeredAppointments,
                                                  Appointment requestAppointment) {
        registeredAppointments.stream()
                .filter(extistingAppointment ->
                        !(extistingAppointment.getAppointmentStatus().equals(AppointmentStatus.CANCELADO) ||
                                extistingAppointment.getAppointmentStatus().equals(AppointmentStatus.FINALIZADO)))
                .forEach(existingAppointment -> existingAppointment
                .validate(requestAppointment));
    }

    public static List<EmployeeScheduleTimeResponse> getAvailableEmployeesFilteredByTimes(List<Employee> availableEmployees,
                                                                                          List<Appointment> appointmentsOfDay,
                                                                                          LocalDate evaluatedDate,
                                                                                          LocalDateTime today
    ) {
        return availableEmployees.stream()
                .map(filteredEmployee ->
                        createWithFilteredAvailableTimes(filteredEmployee, appointmentsOfDay, evaluatedDate, today)
                )
                .toList();
    }


    public static List<EmployeeScheduleTimeResponse> getEmployeesWithPossibleTimesForSpecialty(List<Employee> availableEmployees,
                                                                                               UUID specialtyId,
                                                                                               List<Appointment> appointmentsOfDay,
                                                                                               LocalDate evaluatedDate,
                                                                                               LocalDateTime today
    ) {
        return availableEmployees.stream()
                .filter(employee -> employee.makesSpecialty(specialtyId))
                .map(filteredEmployees ->
                        createWithFilteredPossibleTimesForSpecialty(filteredEmployees, appointmentsOfDay, specialtyId, evaluatedDate, today)
                )
                .toList();
    }

}
