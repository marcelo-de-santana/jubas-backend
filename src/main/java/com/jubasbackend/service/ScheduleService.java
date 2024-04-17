package com.jubasbackend.service;

import com.jubasbackend.controller.response.ScheduleResponse;
import com.jubasbackend.domain.entity.DayAvailability;
import com.jubasbackend.domain.entity.NonServiceDay;
import com.jubasbackend.domain.repository.AppointmentRepository;
import com.jubasbackend.domain.repository.DayAvailabilityRepository;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.domain.repository.NonServiceDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.jubasbackend.utils.AppointmentsUtils.getAvailableEmployeesFilteredByTimes;
import static com.jubasbackend.utils.AppointmentsUtils.getEmployeesWithPossibleTimesForSpecialty;
import static com.jubasbackend.utils.DateTimeUtils.*;
import static com.jubasbackend.utils.DaysOfAttendanceUtils.*;

@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final NonServiceDayRepository nonServiceDayRepository;
    private final DayAvailabilityRepository dayAvailabilityRepository;

    public List<ScheduleResponse> getSchedules(UUID specialtyId) {
        var today = BRAZILIAN_DATETIME;
        var daysAvailableQuantity = dayAvailabilityRepository.findQuantity();
        var lastDay = today.plusDays(daysAvailableQuantity);

        var nonServiceDays = nonServiceDayRepository.findDateBetween(today.toLocalDate(), lastDay.toLocalDate());
        var appointmentsOfPeriod = appointmentRepository.findAllByDateBetween(parseStatOfDay(today), parseEndOfDay(lastDay));
        var availableEmployees = employeeRepository.findAllByActiveProfile();

        var schedule = new ArrayList<ScheduleResponse>();

        // ITERANDO SOBRE CADA DIA DISPONÍVEL PARA AGENDAMENTO
        for (int i = 0; i < daysAvailableQuantity; i++) {

            // CALCULANDO A DATA A SER AVALIADA
            var evaluatedDate = today.plusDays(i).toLocalDate();

            if (!isServiceAvailableOnDay(nonServiceDays, evaluatedDate)) {
                schedule.add(ScheduleResponse.notAvailable(evaluatedDate));
                break;
            }

            // FILTRANDO OS COMPROMISSOS DO DIA AVALIADO
            var appointmentsOfDay = appointmentsOfPeriod.stream()
                    .filter(appointment ->
                            appointment.getDate().toLocalDate().equals(evaluatedDate))
                    .toList();

            // SE A ESPECIALIDADE FOR NULA BUSCA OS HORÁRIOS POSSÍVEIS, SE NÃO BUSCA OS POSSÍVEIS PARA ATENDER A ESPECIALIDADE
            var employeeSchedules = specialtyId != null ?
                    getEmployeesWithPossibleTimesForSpecialty(availableEmployees, specialtyId, appointmentsOfDay, evaluatedDate, today) :
                    getAvailableEmployeesFilteredByTimes(availableEmployees, appointmentsOfDay, evaluatedDate, today);

            // SE NÃO HOUVER HORÁRIOS DISPONÍVEIS, O DIA É MARCADO COMO NÃO DISPONÍVEL; CASO CONTRÁRIO, ADICIONAMOS OS HORÁRIOS DISPONÍVEIS.
            schedule.add(employeeSchedules.isEmpty() ?
                    ScheduleResponse.notAvailable(evaluatedDate) :
                    new ScheduleResponse.WithEmployees(evaluatedDate, employeeSchedules));
        }
        return schedule;
    }

    public List<ScheduleResponse> getDaysOfAttendance(LocalDate startDate, LocalDate endDate) {
        var daysForAppointments = dayAvailabilityRepository.findQuantity();

        // ATRIBUI O DIA ATUAL SE UM DAS DATAS NÃO FOREM PASSADAS
        if (startDate == null || endDate == null) {
            startDate = LocalDate.now();
            endDate = startDate.plusDays(daysForAppointments);
        }

        // SE A ORDEM ESTIVER ERRADA ATRIBUI O DIA ATUAL
        endDate = endDate.isBefore(startDate) ? startDate : endDate;

        var nonServiceDays = nonServiceDayRepository.findDateBetween(startDate, endDate);
        var serviceDays = new ArrayList<ScheduleResponse>();

        addAvailableDayOrFindNext(serviceDays, nonServiceDays, startDate, nonServiceDayRepository);
        generateDaysForThePeriod(serviceDays, nonServiceDays, startDate, endDate);

        return serviceDays;

    }

    public DayAvailability getRangeOfAttendanceDays() {
        return dayAvailabilityRepository.findSingleEntity();
    }

    public void registerDaysWithoutAttendance(List<LocalDate> dates) {
        nonServiceDayRepository.saveAll(dates.stream().map(NonServiceDay::new).toList());
    }

    public void updateRangeOfAttendanceDays(int intervalOfDays) {
        if (intervalOfDays < 0)
            throw new IllegalArgumentException("Interval of days must be a positive integer.");

        var currentDayAvailability = dayAvailabilityRepository.findSingleEntity();

        currentDayAvailability.setQuantity(intervalOfDays);
        dayAvailabilityRepository.save(currentDayAvailability);
    }

    public void deleteDaysWithoutAttendance(List<LocalDate> dates) {
        nonServiceDayRepository.deleteAll(dates.stream().map(NonServiceDay::new).toList());
    }
}
