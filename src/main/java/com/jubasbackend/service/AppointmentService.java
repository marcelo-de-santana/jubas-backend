package com.jubasbackend.service;

import com.jubasbackend.controller.request.AppointmentRequest;
import com.jubasbackend.controller.response.AppointmentResponse;
import com.jubasbackend.domain.entity.*;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.domain.entity.enums.PaymentMethod;
import com.jubasbackend.domain.repository.AppointmentRepository;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.domain.repository.PaymentRepository;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.exception.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.jubasbackend.utils.AppointmentsUtils.getDateTimeForAppointment;
import static com.jubasbackend.utils.AppointmentsUtils.validateAppointmentOverlap;
import static com.jubasbackend.utils.DateTimeUtils.parseEndOfDay;
import static com.jubasbackend.utils.DateTimeUtils.parseStatOfDay;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProfileRepository profileRepository;
    private final PaymentRepository paymentRepository;
    private final MailService mailService;

    public List<AppointmentResponse> getAppointments(LocalDate date) {
        var appointmentsOfDay = appointmentRepository.findAllByDateBetween(parseStatOfDay(date), parseEndOfDay(date));

        return appointmentsOfDay.stream()
                .map(AppointmentResponse::new)
                .toList();
    }

    public AppointmentResponse getAppointment(UUID appointmentId) {
        return new AppointmentResponse(appointmentRepository.findAppointment(appointmentId));
    }

    public List<AppointmentResponse> getAppointmentsByUser(UUID userId) {
        return appointmentRepository.findAllByClient_UserId(userId).stream()
                .map(AppointmentResponse::new)
                .toList();
    }

    public Appointment createAppointment(AppointmentRequest request) {
        var employee = findEmployee(request.employeeId());

        var client = findClient(request.clientId());

        var newAppointment = Appointment.builder()
                .employee(employee)
                .client(client)
                .specialty(employee.getSpecialty(request.specialtyId()))
                .date(request.dateTime())
                .appointmentStatus(AppointmentStatus.MARCADO)
                .createdAt(Instant.now())
                .build();

        newAppointment.validateIfEmployeeMakesSpecialty();

        var registeredAppointments = findAppointments(
                request.date(), request.employeeId(), request.clientId());

        validateAppointmentOverlap(registeredAppointments, newAppointment);

        var savedAppointment = appointmentRepository.save(newAppointment);

        savedAppointment.sendAppointmentNotification(mailService);

        return savedAppointment;
    }

    public void updateAppointment(UUID appointmentId, AppointmentRequest request) {
        var appointment = appointmentRepository.findAppointment(appointmentId);

        //VERIFICA SE É OUTRO FUNCIONÁRIO
        if (request.employeeId() != null)
            appointment.setEmployee(findEmployee(request.employeeId()));

        if (request.specialtyId() != null)
            appointment.setSpecialty(Specialty.builder()
                    .id(request.specialtyId())
                    .build());

        if (request.dateTime() != null)
            appointment.setDate(request.dateTime());

        if (request.clientId() != null)
            appointment.setClient(Profile.builder()
                    .id(request.clientId())
                    .build());

        if (appointmentHasBeenChanged(request)) {
            appointment.validateIfEmployeeMakesSpecialty();

            var registeredAppointments = findAppointments(appointment.getDate().toLocalDate(),
                    appointment.getEmployee().getId(), appointment.getClient().getId());

            validateAppointmentOverlap(registeredAppointments, appointment);
        }

        if (request.appointmentStatus() != null) {
            appointment.setAppointmentStatus(request.appointmentStatus());
        }

        appointment.setUpdatedAt(Instant.now());

        var updatedAppointment = appointmentRepository.save(appointment);

        if (updatedAppointment.getAppointmentStatus().equals(AppointmentStatus.PAGO)) {
            registerPayment(appointment);
        }

        updatedAppointment.sendAppointmentNotification(mailService);

    }

    private List<Appointment> findAppointments(LocalDate requestDate, UUID employeeId, UUID clientId) {
        var date = getDateTimeForAppointment(requestDate);
        return appointmentRepository.findAllByDateBetweenAndEmployeeIdOrClientId(date, parseEndOfDay(date), employeeId, clientId);
    }

    private Employee findEmployee(UUID employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't registered."));
    }

    private Profile findClient(UUID clientId) {
        return profileRepository.findById(clientId).orElseThrow(
                () -> new APIException(HttpStatus.NOT_FOUND, "Client doesn't' registered."));
    }

    private boolean appointmentHasBeenChanged(AppointmentRequest request) {
        return (request.employeeId() != null ||
                request.specialtyId() != null ||
                request.dateTime() != null ||
                request.clientId() != null);
    }

    private void registerPayment(Appointment appointment) {

        var payment = Payment.builder()
                .id(appointment.getId())
                .appointment(appointment)
                .method(PaymentMethod.DINHEIRO)
                .build();

        paymentRepository.save(payment);
    }
}
