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
import java.util.*;

import static com.jubasbackend.utils.DateTimeUtils.*;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final NonServiceDayRepository nonServiceDayRepository;
    private final DayAvailabilityRepository dayAvailableRepository;

    @Override
    public List<ScheduleResponse> findAppointments(Optional<LocalDate> requestDate, Optional<UUID> specialtyId) {
        var employees = employeeRepository.findAll();
        if (employees.isEmpty())
            throw new NoSuchElementException("No employees.");

        var appointments = findAppointmentsInTheRepository(requestDate);

        if (specialtyId.isPresent())
            return getPossibleTimesBySpecialty(specialtyId.get(), employees, appointments);

        if (appointments.isEmpty())
            return employees.stream().map(ScheduleResponse::new).toList();

        return employees.stream().map(employee -> new ScheduleResponse(employee, appointments)).toList();
    }

    @Override
    public AppointmentResponse findAppointment(UUID appointmentId) {
        return new AppointmentResponse(findAppointmentInTheRepository(appointmentId));
    }

    @Override
    public List<String> findDayOfAttendance() {
        var currentDate = LocalDate.now();
        var nonServiceDays = nonServiceDayRepository.getDates();
        var intervalOfDaysToAppointments = dayAvailableRepository.findQuantity();
        var serviceDays = new ArrayList<String>();

        if (isServiceAvailableOnDay(nonServiceDays, currentDate))
            serviceDays.add(currentDate.toString());

        if (intervalOfDaysToAppointments <= 0)
            return serviceDays;

        for (int i = 1; i <= intervalOfDaysToAppointments; i++) {
            var evaluatedDay = currentDate.plusDays(i);
            if (isServiceAvailableOnDay(nonServiceDays, evaluatedDay))
                serviceDays.add(evaluatedDay.toString());
        }

        return serviceDays;
    }

    @Override
    public AppointmentEntity createAppointment(AppointmentCreateRequest request) {
        var employee = findEmployeeInTheRepository(request.employeeId());

        if (!employee.makesSpecialty(request.specialtyId()))
            throw new IllegalArgumentException("Employee doesn't makes specialty.");

        var registeredAppointments = findAppointmentsInTheRepository(request.date(), request.employeeId(), request.clientId());
        var newAppointment = new AppointmentEntity(request, employee);

        validateAppointment(registeredAppointments, newAppointment);

        return appointmentRepository.save(newAppointment);
    }

    @Override
    public void registerDaysWithoutAttendance(DaysWithoutAttendanceRequest request) {
        //TODO: Sistema de envio de e-mail, para notificar clientes desmarcados
        nonServiceDayRepository.saveAll(request.dates().stream().map(NonServiceDayEntity::new).toList());
    }

    @Override
    public void updateDaysOfAttendance(DaysOfAttendanceRequest request) {
        if (request == null || request.intervalOfDays() < 0)
            throw new IllegalArgumentException("Interval of days must be a positive integer.");

        var currentDayAvailability = dayAvailableRepository.findSingleEntity();

        currentDayAvailability.setQuantity(request.intervalOfDays());
        dayAvailableRepository.save(currentDayAvailability);
    }

    @Override
    public void updateAppointment(UUID appointmentId, AppointmentUpdateRequest request) {
        var appointmentToUpdate = findAppointmentInTheRepository(appointmentId);

        //VERIFICA SE É OUTRO FUNCIONÁRIO
        if (request.employeeId() != null && (!appointmentToUpdate.getEmployee().isEqual(request.employeeId())))
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

        validateAppointment(registeredAppointments, appointmentToUpdate);
        appointmentRepository.save(appointmentToUpdate);

    }

    @Override
    public void cancelAppointment(UUID appointmentId) {
        var appointmentToCancel = findAppointmentInTheRepository(appointmentId);

        //VERIFICA SE O HORÁRIO JÁ PASSOU PARA MARCAR COMO CANCELADO, SENÃO EXCLUI
        if (LocalDateTime.now().isAfter(appointmentToCancel.getDate())) {
            appointmentToCancel.setAppointmentStatus(AppointmentStatus.CANCELADO);
            appointmentRepository.save(appointmentToCancel);
        } else {
            appointmentRepository.delete(appointmentToCancel);
        }
    }

    @Override
    public void deleteDaysWithoutAttendance(DaysWithoutAttendanceRequest request) {
        nonServiceDayRepository.deleteAll(request.dates().stream().map(NonServiceDayEntity::new).toList());
    }

    private List<AppointmentEntity> findAppointmentsInTheRepository(Optional<LocalDate> date) {
        var selectedDate = getSelectedDate(date);
        return appointmentRepository.findAllByDateBetween(selectedDate, getEndDay(selectedDate));
    }

    private List<AppointmentEntity> findAppointmentsInTheRepository(LocalDate requestDate, UUID employeeId, UUID clientId) {
        var date = getCurrentOrFutureDate(requestDate);
        return appointmentRepository.findAllByDateBetweenAndEmployeeIdOrClientId(date, getEndDay(date), employeeId, clientId);
    }

    private EmployeeEntity findEmployeeInTheRepository(UUID employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't registered."));
    }

    private AppointmentEntity findAppointmentInTheRepository(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new NoSuchElementException("Appointment not found."));
    }

    private void validateAppointment(List<AppointmentEntity> registeredAppointments, AppointmentEntity requestAppointment) {
        if (!registeredAppointments.isEmpty())
            registeredAppointments.forEach(existingAppointment -> existingAppointment.validate(requestAppointment));
    }

    private List<ScheduleResponse> getPossibleTimesBySpecialty(
            UUID specialtyId,
            List<EmployeeEntity> employees,
            List<AppointmentEntity> appointments
    ) {
        var filteredEmployees = employees.stream().filter(employee -> employee.makesSpecialty(specialtyId)).toList();

        if (filteredEmployees.isEmpty())
            throw new IllegalArgumentException("No employee carries out the specialty.");

        var possibleTimes = filteredEmployees.stream()
                .map(filteredEmployee -> new ScheduleResponse(
                        filteredEmployee.getId(),
                        filteredEmployee.getProfile().getName(),
                        filteredEmployee.getPossibleTimes(specialtyId, appointments))).toList();

        if (possibleTimes.isEmpty())
            throw new NoSuchElementException("No available time slots for the service.");

        return possibleTimes;
    }

    //VERIFICA SE HÁ ATENDIMENTO NO DIA ESPECÍFICO
    public boolean isServiceAvailableOnDay(List<LocalDate> nonServiceDays, LocalDate evaluatedDay) {
        return (nonServiceDays.stream()
                .noneMatch(dayWithoutService -> dayWithoutService.getDayOfYear() == evaluatedDay.getDayOfYear()));
    }
}
