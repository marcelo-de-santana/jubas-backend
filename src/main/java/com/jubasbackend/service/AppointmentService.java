package com.jubasbackend.service;

import com.jubasbackend.controller.request.AppointmentRequest;
import com.jubasbackend.controller.response.AppointmentResponse;
import com.jubasbackend.controller.response.DaysOfAttendanceResponse;
import com.jubasbackend.controller.response.ScheduleResponse;
import com.jubasbackend.domain.entity.*;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.repository.AppointmentRepository;
import com.jubasbackend.domain.repository.DayAvailabilityRepository;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.domain.repository.NonServiceDayRepository;
import com.jubasbackend.exception.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.jubasbackend.utils.AppointmentsUtils.*;
import static com.jubasbackend.utils.DateTimeUtils.*;
import static com.jubasbackend.utils.DaysOfAttendanceUtils.*;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final NonServiceDayRepository nonServiceDayRepository;
    private final DayAvailabilityRepository dayAvailabilityRepository;
    private final MailService mailService;

    public List<ScheduleResponse> findAppointments(LocalDate requestDate, UUID specialtyId, boolean toFilter) {
        var dateTime = obtainDateTimeFromOptionalDate(requestDate);
        var filterTime = dateTime.toLocalTime();
        var availableEmployees = findAvailableEmployees(employeeRepository);
        var appointments = findAppointmentsOfDay(dateTime);

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

        addAvailableServiceDayOrFindNext(
                serviceDays,
                nonServiceDays,
                optionalStartDate,
                optionalEndDate,
                startOfPeriod,
                nonServiceDayRepository);

        if (optionalStartDate != null || optionalEndDate != null) {
            generateDaysForThePeriod(serviceDays, nonServiceDays, startOfPeriod, endOfPeriod);
            return serviceDays;
        }

        generateDaysAccordingToTheRangeOfDays(serviceDays, nonServiceDays, rangeOfDaysForAppointments);
        return serviceDays;
    }

    public Appointment createAppointment(AppointmentRequest request) {
        var employee = findEmployeeInTheRepository(request.employeeId());

        if (!employee.makesSpecialty(request.specialtyId()))
            throw new APIException(NOT_FOUND, "Employee doesn't makes specialty.");

        var registeredAppointments = findAppointmentsInTheRepository(request.date(), request.employeeId(), request.clientId());
        var newAppointment = Appointment.create(request, employee);

        validateAppointmentOverlap(registeredAppointments, newAppointment);

        return appointmentRepository.save(newAppointment);
    }

    public void registerDaysWithoutAttendance(List<LocalDate> dates) {
        //TODO: Sistema de envio de e-mail, para notificar clientes desmarcados
        nonServiceDayRepository.saveAll(dates.stream().map(NonServiceDay::new).toList());
    }

    public void updateRangeOfAttendanceDays(int intervalOfDays) {
        if (intervalOfDays < 0)
            throw new IllegalArgumentException("Interval of days must be a positive integer.");

        var currentDayAvailability = dayAvailabilityRepository.findSingleEntity();

        currentDayAvailability.setQuantity(intervalOfDays);
        dayAvailabilityRepository.save(currentDayAvailability);
    }

    public void updateAppointment(UUID appointmentId, AppointmentRequest request) {
        var appointmentToUpdate = findAppointmentInTheRepository(appointmentId);

        //VERIFICA SE É OUTRO FUNCIONÁRIO
        if (request.employeeId() != null && (!appointmentToUpdate.getEmployee().hasId(request.employeeId())))
            appointmentToUpdate.setEmployee(findEmployeeInTheRepository(request.employeeId()));

        if (request.specialtyId() != null)
            appointmentToUpdate.setSpecialty(Specialty.builder().id(request.specialtyId()).build());

        if (request.clientId() != null)
            appointmentToUpdate.setClient(Profile.builder().id(request.clientId()).build());

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

    public void cancelAppointment(UUID appointmentId, JwtAuthenticationToken jwt) {
        var appointment = findAppointmentInTheRepository(appointmentId);
        var userIdRequest = jwt.getName();

        var clientRequested = userIdRequest
                .equals(appointment
                        .getClient()
                        .getUser()
                        .getId()
                        .toString());

        if (appointment.expiredTime()) {
            if (clientRequested) {
                appointment.sendCancellationNotificationByClientWhenExpiredTime(mailService);
            } else {
                appointment.sendCancellationNotificationByEmployeeWhenExpiredTime(mailService);
            }
            appointment.setAppointmentStatus(AppointmentStatus.CANCELADO);
            appointmentRepository.save(appointment);
            return;
        }
        if (clientRequested) {
            appointment.sendCancellationNotificationByClient(mailService);
        }
        appointment.sendCancellationNotificationByEmployee(mailService);

        appointmentRepository.delete(appointment);
    }

    public void deleteDaysWithoutAttendance(List<LocalDate> dates) {
        nonServiceDayRepository.deleteAll(dates.stream().map(NonServiceDay::new).toList());
    }

    private List<Appointment> findAppointmentsOfDay(LocalDateTime date) {
        return appointmentRepository.findAllByDateBetween(parseStatOfDay(date), parseEndOfDay(date));
    }

    private List<Appointment> findAppointmentsInTheRepository(LocalDate requestDate, UUID employeeId, UUID clientId) {
        var date = getDateTimeForAppointment(requestDate);
        return appointmentRepository.findAllByDateBetweenAndEmployeeIdOrClientId(date, parseEndOfDay(date), employeeId, clientId);
    }

    private Employee findEmployeeInTheRepository(UUID employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't registered."));
    }

    private Appointment findAppointmentInTheRepository(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new NoSuchElementException("Appointment not found."));
    }

}
