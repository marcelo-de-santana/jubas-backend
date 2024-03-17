package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.appointment.enums.AppointmentStatus;
import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.exception.ConflictException;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tb_appointment")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private ProfileEntity client;
    @ManyToOne
    @JoinColumn(name = "specialty_id")
    private SpecialtyEntity specialty;
    private AppointmentStatus appointmentStatus;
    private LocalDateTime date;
    private Instant createdAt;
    private Instant updatedAt;

    public AppointmentEntity(AppointmentCreateRequest request, EmployeeEntity employee) {
        this.client = ProfileEntity.builder().id(request.clientId()).build();
        this.employee = employee;
        this.specialty = employee.getSpecialty(request.specialtyId());
        this.appointmentStatus = AppointmentStatus.MARCADO;
        this.date = request.dateTime();
        this.createdAt = Instant.now();
    }

    public LocalTime startTime() {
        return getDate().toLocalTime();
    }

    public LocalTime endTime() {
        var duration = getSpecialty().getTimeDuration();
        var startTime = startTime();
        return startTime.plusHours(duration.getHour()).plusMinutes(duration.getMinute());
    }

    public boolean isInThePeriod(LocalTime time) {
        return (time.equals(startTime()) || (time.isAfter(startTime()) && time.isBefore(endTime())));
    }

    /**
     * Recebe o novo agendamento e compara com os atuais para aplicar as regras de negócio.
     * @param newAppointment
     */
    public void validate(AppointmentEntity newAppointment) {
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
    private void validateSameSpecialty(SpecialtyEntity specialty, ProfileEntity client) {
        if (getAppointmentStatus().equals(AppointmentStatus.MARCADO)&& specialty.getId().equals(getSpecialty().getId())
                && client.getId().equals(getClient().getId())) {
            throw new IllegalArgumentException("The same profile cannot schedule two services for the same day.");
        }
    }

    //VERIFICA SE O FIM DO ATENDIMENTO NÃO EXCEDE O INÍCIO DO PRÓXIMO
    private void validateEndTimeOverlap(LocalTime endTime) {
        if (endTime.isAfter(getDate().toLocalTime()) && endTime.isBefore(endTime()))
            throw new ConflictException("The end time of the service must not occur after the start time of another service.");
    }

    //VERIFICA SE O INÍCIO DO ATENDIMENTO NÃO SOBRESCREVE O FIM DO ANTERIOR
    private void validateStartTimeOverlap(LocalTime startTime) {
        if (startTime.isAfter(getDate().toLocalTime()) && startTime.isBefore(endTime()))
            throw new ConflictException("The start time of the service must not occur before the end time of another service.");
    }

    //VERIFICA SE NÃO HÁ AGENDAMENTO DENTRO DO PERÍODO
    private void validateWithinTimePeriod(LocalTime startTime, LocalTime endTime) {
        if (startTime.isBefore(getDate().toLocalTime()) && endTime.isAfter(endTime()))
            throw new ConflictException("There is another appointment scheduled within the specified time period.");
    }

    //VERIFICA SE O INÍCIO OU O FIM DO NOVO HORÁRIO COINCIDE COM O AGENDADO
    private void validateStartOrEndConflict(LocalTime startTime, LocalTime endTime) {
        if (startTime.equals(getDate().toLocalTime()) || endTime.equals(endTime()))
            throw new ConflictException("The start or end of the new schedule conflicts with the booked one.");
    }
}
