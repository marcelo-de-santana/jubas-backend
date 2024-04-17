package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.entity.Employee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EmployeeScheduleTimeResponse(UUID employeeId, String employeeName, List<ScheduleTimeResponse> workingHours) {

    public static EmployeeScheduleTimeResponse createWithAvailableTimes(Employee employee,
                                                                        List<Appointment> appointmentsOfDay) {
        return new EmployeeScheduleTimeResponse(
                employee.getId(),
                employee.getProfile().getName(),
                employee.getAvailableTimes(appointmentsOfDay));
    }

    public static EmployeeScheduleTimeResponse createWithFilteredAvailableTimes(Employee employee,
                                                                                List<Appointment> appointmentsOfDay,
                                                                                LocalDate evaluatedDate,
                                                                                LocalDateTime today) {
        return new EmployeeScheduleTimeResponse(
                employee.getId(),
                employee.getProfile().getName(),
                filterAvailableTimes(employee.getAvailableTimes(appointmentsOfDay), evaluatedDate, today)
        );

    }

    public static EmployeeScheduleTimeResponse createWithFilteredPossibleTimesForSpecialty(Employee employee,
                                                                                           List<Appointment> appointmentsOfDay,
                                                                                           UUID specialtyId,
                                                                                           LocalDate evaluatedDate,
                                                                                           LocalDateTime today) {
        return new EmployeeScheduleTimeResponse(
                employee.getId(),
                employee.getProfile().getName(),
                filterAvailableTimes(employee.getPossibleTimes(specialtyId, appointmentsOfDay), evaluatedDate, today)
        );
    }

    //FILTRA HORÁRIOS DISPONÍVEIS
    private static List<ScheduleTimeResponse> filterAvailableTimes(List<ScheduleTimeResponse> schedules,
                                                                   LocalDate evaluatedDate,
                                                                   LocalDateTime today) {
        return schedules.stream()
                .filter(ScheduleTimeResponse::isAvailable)

                //FILTRA HORÁRIOS SE O DIA ANALISADO FOR HOJE
                .filter(schedule ->
                        !evaluatedDate.equals(today.toLocalDate()) || schedule.getTime().isAfter(today.toLocalTime()))
                .toList();
    }

}
