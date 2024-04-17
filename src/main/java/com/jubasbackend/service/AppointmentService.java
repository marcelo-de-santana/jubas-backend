package com.jubasbackend.service;

import com.jubasbackend.controller.request.AppointmentRequest;
import com.jubasbackend.controller.response.AppointmentResponse;
import com.jubasbackend.controller.response.EmployeeScheduleTimeResponse;
import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.Specialty;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.repository.AppointmentRepository;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.exception.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.jubasbackend.controller.response.EmployeeScheduleTimeResponse.createWithAvailableTimes;
import static com.jubasbackend.utils.AppointmentsUtils.getDateTimeForAppointment;
import static com.jubasbackend.utils.AppointmentsUtils.validateAppointmentOverlap;
import static com.jubasbackend.utils.DateTimeUtils.parseEndOfDay;
import static com.jubasbackend.utils.DateTimeUtils.parseStatOfDay;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final MailService mailService;

    public List<EmployeeScheduleTimeResponse> getAppointments(LocalDate date) {
        var availableEmployees = employeeRepository.findAllByActiveProfile();
        var appointmentsOfDay = findAppointmentsOfDay(date);

        return availableEmployees.stream()
                .map(employee -> createWithAvailableTimes(employee, appointmentsOfDay))
                .toList();
    }

    public AppointmentResponse getAppointment(UUID appointmentId) {
        return new AppointmentResponse(findAppointment(appointmentId));
    }

    public Appointment createAppointment(AppointmentRequest request) {
        var employee = findEmployee(request.employeeId());

        if (!employee.makesSpecialty(request.specialtyId()))
            throw new APIException(NOT_FOUND, "Employee doesn't makes specialty.");

        var registeredAppointments = findAppointments(request.date(), request.employeeId(), request.clientId());
        var newAppointment = Appointment.create(request, employee);

        validateAppointmentOverlap(registeredAppointments, newAppointment);

        return appointmentRepository.save(newAppointment);
    }

    public void updateAppointment(UUID appointmentId, AppointmentRequest request) {
        var appointmentToUpdate = findAppointment(appointmentId);

        //VERIFICA SE É OUTRO FUNCIONÁRIO
        if (request.employeeId() != null && (!appointmentToUpdate.getEmployee().hasId(request.employeeId())))
            appointmentToUpdate.setEmployee(findEmployee(request.employeeId()));

        if (request.specialtyId() != null)
            appointmentToUpdate.setSpecialty(Specialty.builder()
                    .id(request.specialtyId())
                    .build());

        if (request.clientId() != null)
            appointmentToUpdate.setClient(Profile.builder()
                    .id(request.clientId())
                    .build());

        if (request.dateTime() != null)
            appointmentToUpdate.setDate(request.dateTime());

        if (request.appointmentStatus() != null)
            appointmentToUpdate.setAppointmentStatus(request.appointmentStatus());

        appointmentToUpdate.setUpdatedAt(Instant.now());

        //VERIFICA SE O FUNCIONÁRIO REALIZA O SERVIÇO
        appointmentToUpdate.validateIfEmployeeMakesSpecialty();

        var registeredAppointments = findAppointments(
                appointmentToUpdate.getDate().toLocalDate(),
                appointmentToUpdate.getEmployee().getId(),
                appointmentToUpdate.getClient().getId());

        validateAppointmentOverlap(registeredAppointments, appointmentToUpdate);
        appointmentRepository.save(appointmentToUpdate);

    }

    public void cancelAppointment(UUID appointmentId, JwtAuthenticationToken jwt) {
        var appointment = findAppointment(appointmentId);
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

    private List<Appointment> findAppointmentsOfDay(LocalDate date) {
        return appointmentRepository.findAllByDateBetween(parseStatOfDay(date), parseEndOfDay(date));
    }

    private List<Appointment> findAppointments(LocalDate requestDate, UUID employeeId, UUID clientId) {
        var date = getDateTimeForAppointment(requestDate);
        return appointmentRepository.findAllByDateBetweenAndEmployeeIdOrClientId(date, parseEndOfDay(date), employeeId, clientId);
    }

    private Employee findEmployee(UUID employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't registered."));
    }

    private Appointment findAppointment(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new NoSuchElementException("Appointment not found."));
    }

}
