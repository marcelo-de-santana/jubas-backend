package com.jubasbackend.service;

import com.jubasbackend.controller.request.AppointmentCreateRequest;
import com.jubasbackend.controller.request.AppointmentUpdateRequest;
import com.jubasbackend.controller.request.RangeOfAttendanceRequest;
import com.jubasbackend.controller.response.AppointmentResponse;
import com.jubasbackend.controller.response.DaysOfAttendanceResponse;
import com.jubasbackend.controller.response.ScheduleResponse;
import com.jubasbackend.domain.entity.*;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.repository.AppointmentRepository;
import com.jubasbackend.domain.repository.DayAvailabilityRepository;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.domain.repository.NonServiceDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.jubasbackend.utils.AppointmentsUtils.*;
import static com.jubasbackend.utils.DateTimeUtils.*;
import static com.jubasbackend.utils.DaysOfAttendanceUtils.*;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final NonServiceDayRepository nonServiceDayRepository;
    private final DayAvailabilityRepository dayAvailabilityRepository;

    public List<ScheduleResponse> findAppointments(LocalDate requestDate, UUID specialtyId, boolean toFilter) {
        var dateTime = obtainDateTimeFromOptionalDate(requestDate);
        var filterTime = dateTime.toLocalTime();
        var availableEmployees = findAvailableEmployees(employeeRepository);
        var appointments = findAppointmentsOfDayInTheRepository(dateTime);

        return generateScheduleResponses(requestDate, specialtyId, toFilter, filterTime, availableEmployees, appointments);

    }

    public AppointmentResponse findAppointment(UUID appointmentId) {
        return new AppointmentResponse(findAppointmentInTheRepository(appointmentId));
    }

    public List<DaysOfAttendanceResponse> findDaysOfAttendance(LocalDate optionalStartDate, LocalDate optionalEndDate) {
        var rangeOfDaysForAppointments = dayAvailabilityRepository.findQuantity();
        var startOfPeriod = optionalStartDate != null ? optionalStartDate : LocalDate.now();
        var endOfPeriod = applyEndDateLimits(optionalStartDate, optionalEndDate, startOfPeriod, rangeOfDaysForAppointments);

        var nonServiceDays = nonServiceDayRepository.findDateBetween(startOfPeriod, endOfPeriod);
        var serviceDays = new ArrayList<DaysOfAttendanceResponse>();

        addAvailableServiceDayOrFindNext(serviceDays, nonServiceDays, optionalStartDate, optionalEndDate, startOfPeriod, nonServiceDayRepository);

        if (optionalStartDate != null || optionalEndDate != null) {
            generateDaysForThePeriod(serviceDays, nonServiceDays, startOfPeriod, endOfPeriod);
            return serviceDays;
        }

        generateDaysAccordingToTheRangeOfDays(serviceDays, nonServiceDays, startOfPeriod, rangeOfDaysForAppointments);
        return serviceDays;
    }

    public AppointmentEntity createAppointment(AppointmentCreateRequest request) {
        var employee = findEmployeeInTheRepository(request.employeeId());

        if (!employee.makesSpecialty(request.specialtyId()))
            throw new IllegalArgumentException("Employee doesn't makes specialty.");

        var registeredAppointments = findAppointmentsInTheRepository(request.date(), request.employeeId(), request.clientId());
        var newAppointment = new AppointmentEntity(request, employee);

        validateAppointmentOverlap(registeredAppointments, newAppointment);

        return appointmentRepository.save(newAppointment);
    }

    public void registerDaysWithoutAttendance(List<LocalDate> dates) {
        //TODO: Sistema de envio de e-mail, para notificar clientes desmarcados
        nonServiceDayRepository.saveAll(dates.stream().map(NonServiceDayEntity::new).toList());
    }

    public void updateRangeOfAttendanceDays(RangeOfAttendanceRequest request) {
        if (request == null || request.intervalOfDays() < 0)
            throw new IllegalArgumentException("Interval of days must be a positive integer.");

        var currentDayAvailability = dayAvailabilityRepository.findSingleEntity();

        currentDayAvailability.setQuantity(request.intervalOfDays());
        dayAvailabilityRepository.save(currentDayAvailability);
    }

    public void updateAppointment(UUID appointmentId, AppointmentUpdateRequest request) {
        var appointmentToUpdate = findAppointmentInTheRepository(appointmentId);

        //VERIFICA SE É OUTRO FUNCIONÁRIO
        if (request.employeeId() != null && (!appointmentToUpdate.getEmployee().hasId(request.employeeId())))
            appointmentToUpdate.setEmployee(findEmployeeInTheRepository(request.employeeId()));

        if (request.specialtyId() != null)
            appointmentToUpdate.setSpecialty(SpecialtyEntity.builder().id(request.specialtyId()).build());

        if (request.clientId() != null)
            appointmentToUpdate.setClient(ProfileEntity.builder().id(request.clientId()).build());

        if (request.dateTime() != null)
            appointmentToUpdate.setDate(request.dateTime());

        //VERIFICA SE O FUNCIONÁRIO REALIZA O SERVIÇO
        appointmentToUpdate.validateIfEmployeeMakesSpecialty();

        var registeredAppointments = findAppointmentsInTheRepository(
                appointmentToUpdate.getDate().toLocalDate(),
                appointmentToUpdate.getEmployee().getId(),
                appointmentToUpdate.getClient().getId());

        validateAppointmentOverlap(registeredAppointments, appointmentToUpdate);
        appointmentRepository.save(appointmentToUpdate);

    }

    public void cancelAppointment(UUID appointmentId) {
        var appointmentToCancel = findAppointmentInTheRepository(appointmentId);
        if (LocalDateTime.now().isAfter(appointmentToCancel.getDate())) {
            appointmentToCancel.setAppointmentStatus(AppointmentStatus.CANCELADO);
            appointmentRepository.save(appointmentToCancel);
        } else {
            appointmentRepository.delete(appointmentToCancel);
        }
    }

    public void deleteDaysWithoutAttendance(List<LocalDate> dates) {
        nonServiceDayRepository.deleteAll(dates.stream().map(NonServiceDayEntity::new).toList());
    }

    private List<AppointmentEntity> findAppointmentsOfDayInTheRepository(LocalDateTime date) {
        return appointmentRepository.findAllByDateBetween(parseStatOfDay(date), parseEndOfDay(date));
    }

    private List<AppointmentEntity> findAppointmentsInTheRepository(LocalDate requestDate, UUID employeeId, UUID clientId) {
        var date = getDateTimeForAppointment(requestDate);
        return appointmentRepository.findAllByDateBetweenAndEmployeeIdOrClientId(date, parseEndOfDay(date), employeeId, clientId);
    }

    private EmployeeEntity findEmployeeInTheRepository(UUID employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't registered."));
    }

    private AppointmentEntity findAppointmentInTheRepository(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(
//                () -> new NoSuchElementException("Appointment not found."));
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
