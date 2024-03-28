package com.jubasbackend.domain.entity;

import com.jubasbackend.controller.request.AppointmentRequest;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import com.jubasbackend.exception.APIException;
import com.jubasbackend.service.MailService;
import com.jubasbackend.utils.MailMessageUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.jubasbackend.utils.DateTimeUtils.BRAZILIAN_DATETIME;
import static org.springframework.http.HttpStatus.CONFLICT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Profile client;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    @NotNull
    private AppointmentStatus appointmentStatus;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private Instant createdAt;

    private Instant updatedAt;

    public static Appointment create(AppointmentRequest request, Employee employee){
        return Appointment.builder()
                .employee(employee)
                .specialty(employee.getSpecialty(request.specialtyId()))
                .appointmentStatus(AppointmentStatus.MARCADO)
                .date(request.dateTime())
                .createdAt(Instant.now())
                .client(Profile.builder()
                        .id(request.clientId())
                        .build())
                .build();
    }

    public LocalTime startTime() {
        return getDate().toLocalTime();
    }

    public LocalTime endTime() {
        var duration = getSpecialty().getTimeDuration();
        var startTime = startTime();
        return startTime.plusHours(duration.getHour()).plusMinutes(duration.getMinute());
    }

    public String getEmployeeName() {
        return employee.getProfile().getName();
    }

    public String getEmployeeEmail() {
        return employee.getProfile().getUser().getEmail();
    }

    public String getClientName() {
        return client.getName();
    }

    public String getClientEmail() {
        return client.getUser().getEmail();
    }

    public boolean isInThePeriod(LocalTime time) {
        return (time.equals(startTime()) || (time.isAfter(startTime()) && time.isBefore(endTime())));
    }

    /**
     * Recebe o novo agendamento e compara com os atuais para aplicar as regras de negócio.
     */
    public void validate(Appointment newAppointment) {
        if (!getId().equals(newAppointment.getId())) {
            validateSameSpecialty(newAppointment.getSpecialty(), newAppointment.client);
            validateStartTimeOverlap(newAppointment.startTime());
            validateEndTimeOverlap(newAppointment.endTime());
            validateStartOrEndConflict(newAppointment.startTime(), newAppointment.endTime());
            validateWithinTimePeriod(newAppointment.startTime(), newAppointment.endTime());
        }
    }

    //VERIFICA SE O FUNCIONÁRIO REALIZA O SERVIÇO
    public void validateIfEmployeeMakesSpecialty() {
        if (!getEmployee().makesSpecialty(specialty.getId()))
            throw new NoSuchElementException("Employee doesn't execute this specialty.");

    }

    //VERIFICA SE O CLIENTE AGENDOU O MESMO SERVIÇO NO DIA
    private void validateSameSpecialty(Specialty specialty, Profile client) {
        if (getAppointmentStatus().equals(AppointmentStatus.MARCADO)
                && specialty.getId().equals(getSpecialty().getId())
                && client.getId().equals(getClient().getId())) {
            throw new APIException(CONFLICT, "The same profile cannot schedule two services for the same day.");
        }
    }

    //VERIFICA SE O FIM DO ATENDIMENTO NÃO EXCEDE O INÍCIO DO PRÓXIMO
    private void validateEndTimeOverlap(LocalTime endTime) {
        if (endTime.isAfter(getDate().toLocalTime()) && endTime.isBefore(endTime()))
            throw new APIException(CONFLICT,
                    "The end time of the service must not occur after the start time of another service.");
    }

    //VERIFICA SE O INÍCIO DO ATENDIMENTO NÃO SOBRESCREVE O FIM DO ANTERIOR
    private void validateStartTimeOverlap(LocalTime startTime) {
        if (startTime.isAfter(getDate().toLocalTime()) && startTime.isBefore(endTime()))
            throw new APIException(CONFLICT,
                    "The start time of the service must not occur before the end time of another service.");
    }

    //VERIFICA SE NÃO HÁ AGENDAMENTO DENTRO DO PERÍODO
    private void validateWithinTimePeriod(LocalTime startTime, LocalTime endTime) {
        if (startTime.isBefore(getDate().toLocalTime()) && endTime.isAfter(endTime()))
            throw new APIException(CONFLICT,
                    "There is another appointment scheduled within the specified time period.");
    }

    //VERIFICA SE O INÍCIO OU O FIM DO NOVO HORÁRIO COINCIDE COM O AGENDADO
    private void validateStartOrEndConflict(LocalTime startTime, LocalTime endTime) {
        if (startTime.equals(getDate().toLocalTime()) || endTime.equals(endTime()))
            throw new APIException(CONFLICT,
                    "The start or end of the new schedule conflicts with the booked one.");
    }

    public boolean expiredTime() {
        return BRAZILIAN_DATETIME.isAfter(getDate());
    }

    public void sendCancellationNotificationByClientWhenExpiredTime(MailService mailService) {
        mailService.
                sendCancellationEmails(
                        getClientEmail(),
                        getEmployeeEmail(),
                        MailMessageUtils.ClientCancellationExpiredTime.messageClient(getClientName(), getEmployeeName(), getDate(), startTime()),
                        MailMessageUtils.ClientCancellationExpiredTime.messageEmployee(getClientName(), getDate(), startTime()),
                        MailMessageUtils.ClientCancellationExpiredTime.messageAdmin(getClientName(), getEmployeeName(), getDate(), startTime())
                );
    }

    public void sendCancellationNotificationByEmployeeWhenExpiredTime(MailService mailService) {
        mailService.sendCancellationEmails(
                getClientEmail(),
                getEmployeeEmail(),
                MailMessageUtils.EmployeeCancellationExpiredTime.clientMessage(getClientName(), getEmployeeName(), getDate(), startTime()),
                MailMessageUtils.EmployeeCancellationExpiredTime.employeeMessage(getClientName(), getDate(), startTime()),
                MailMessageUtils.EmployeeCancellationExpiredTime.adminMessage(getClientName(), getEmployeeName(), getDate(), startTime()));
    }

    public void sendCancellationNotificationByClient(MailService mailService) {
        mailService
                .sendCancellationEmails(
                        getClientEmail(),
                        getEmployeeEmail(),
                        MailMessageUtils.ClientCancellation.clientMessage(getClientName(), getEmployeeName(), getDate(), startTime()),
                        MailMessageUtils.ClientCancellation.employeeMessage(getClientName(), getDate(), startTime()),
                        MailMessageUtils.ClientCancellation.adminMessage(getClientName(), getEmployeeName(), getDate(), startTime()));
    }

    public void sendCancellationNotificationByEmployee(MailService mailService) {
        mailService.sendCancellationEmails(
                getClientEmail(),
                getEmployeeEmail(),
                MailMessageUtils.EmployeeCancellation.clientMessage(getClientName(), getEmployeeName(), getDate(), startTime()),
                MailMessageUtils.EmployeeCancellation.employeeMessage(getClientName(), getDate(), startTime()),
                MailMessageUtils.EmployeeCancellation.adminMessage(getClientName(), getEmployeeName(), getDate(), startTime()));
    }

}
