package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.*;
import com.jubasbackend.core.appointment.enums.AppointmentStatus;
import com.jubasbackend.core.day_availability.DayAvailabilityRepository;
import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.employee.EmployeeRepository;
import com.jubasbackend.core.non_service_day.NonServiceDayEntity;
import com.jubasbackend.core.non_service_day.NonServiceDayRepository;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.jubasbackend.core.appointment.utils.AppointmentsUtils.*;
import static com.jubasbackend.core.appointment.utils.DaysOfAttendanceUtils.*;
import static com.jubasbackend.utils.DateTimeUtils.*;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final NonServiceDayRepository nonServiceDayRepository;
    private final DayAvailabilityRepository dayAvailabilityRepository;

    @Override
    public List<ScheduleResponse> findAppointments(LocalDate requestDate, UUID specialtyId, boolean toFilter) {
        var dateTime = obtainDateTimeFromOptionalDate(requestDate);
        var filterTime = dateTime.toLocalTime();
        var availableEmployees = findAvailableEmployees(employeeRepository);
        var appointments = findAppointmentsOfDayInTheRepository(dateTime);

        return generateScheduleResponses(requestDate, specialtyId, toFilter, filterTime, availableEmployees, appointments);

    }

    @Override
    public AppointmentResponse findAppointment(UUID appointmentId) {
        return new AppointmentResponse(findAppointmentInTheRepository(appointmentId));
    }

    @Override
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

    @Override
    public AppointmentEntity createAppointment(AppointmentCreateRequest request) {
        var employee = findEmployeeInTheRepository(request.employeeId());

        if (!employee.makesSpecialty(request.specialtyId()))
            throw new IllegalArgumentException("Employee doesn't makes specialty.");

        var registeredAppointments = findAppointmentsInTheRepository(request.date(), request.employeeId(), request.clientId());
        var newAppointment = new AppointmentEntity(request, employee);

        validateAppointmentOverlap(registeredAppointments, newAppointment);

        return appointmentRepository.save(newAppointment);
    }

    @Override
    public void registerDaysWithoutAttendance(List<LocalDate> dates) {
        //TODO: Sistema de envio de e-mail, para notificar clientes desmarcados
        nonServiceDayRepository.saveAll(dates.stream().map(NonServiceDayEntity::new).toList());
    }

    @Override
    public void updateRangeOfAttendanceDays(RangeOfAttendanceRequest request) {
        if (request == null || request.intervalOfDays() < 0)
            throw new IllegalArgumentException("Interval of days must be a positive integer.");

        var currentDayAvailability = dayAvailabilityRepository.findSingleEntity();

        currentDayAvailability.setQuantity(request.intervalOfDays());
        dayAvailabilityRepository.save(currentDayAvailability);
    }

    @Override
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

    @Override
    public void cancelAppointment(UUID appointmentId) {
        var appointmentToCancel = findAppointmentInTheRepository(appointmentId);
        if (LocalDateTime.now().isAfter(appointmentToCancel.getDate())) {
            appointmentToCancel.setAppointmentStatus(AppointmentStatus.CANCELADO);
            appointmentRepository.save(appointmentToCancel);
        } else {
            appointmentRepository.delete(appointmentToCancel);
        }
    }

    @Override
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
                () -> new NoSuchElementException("Appointment not found."));
    }


}
