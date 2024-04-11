package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.entity.Employee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EmployeeScheduleResponse(UUID id, String name, List<ScheduleTimeResponse> workingHours) {

    public static EmployeeScheduleResponse createWithAvailableTimes(Employee employee, List<Appointment> appointments) {
        return new EmployeeScheduleResponse(
                employee.getId(),
                employee.getProfile().getName(),
                employee.getWorkingHour().getAvailableTimes(appointments));
    }

    public static EmployeeScheduleResponse createWithFilteredAvailableTimes(Employee employee,
                                                                            List<Appointment> appointmentsOfDay,
                                                                            LocalDate evaluatedDate,
                                                                            LocalDateTime today) {
        return new EmployeeScheduleResponse(
                employee.getId(),
                employee.getProfile().getName(),
                filterAvailableTimes(employee.getWorkingHour().getAvailableTimes(appointmentsOfDay), evaluatedDate, today)
        );

    }

    public static EmployeeScheduleResponse createWithFilteredPossibleTimesForSpecialty(Employee employee,
                                                                                       List<Appointment> appointmentsOfDay,

                                                                                       UUID specialtyId,
                                                                                       LocalDate evaluatedDate,
                                                                                       LocalDateTime today) {
        return new EmployeeScheduleResponse(
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
                        !evaluatedDate.equals(today.toLocalDate()) || schedule.time().isAfter(today.toLocalTime()))
                .toList();
    }

}
