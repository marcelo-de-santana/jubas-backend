package com.jubasbackend.core.appointment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;

    @Override
    public AppointmentEntity createAppointment(AppointmentEntity newAppointment) {

        var existingAppointments = findAppointmentsOnRepository(newAppointment);

        if (!existingAppointments.isEmpty()) {
            for (var existingAppointment : existingAppointments) {
                validateSameSpecialty(newAppointment, existingAppointment);
                validateEndTimeOverlap(newAppointment, existingAppointment);
                validateStartTimeOverlap(newAppointment, existingAppointment);
                validateWithinTimePeriod(newAppointment, existingAppointment);
                validateStartOrEndConflict(newAppointment, existingAppointment);
            }
        }

        return appointmentRepository.save(newAppointment);
    }

    private List<AppointmentEntity> findAppointmentsOnRepository(AppointmentEntity appointment) {
        return appointmentRepository.findAllByDate_DateAndEmployeeIdOrClientId(
                appointment.getDate().toLocalDate(), appointment.getEmployee().getId(), appointment.getClient().getId());
    }

    //VERIFICA SE O CLIENTE AGENDOU O MESMO SERVIÇO NO DIA
    private void validateSameSpecialty(AppointmentEntity newSpecialty, AppointmentEntity existingAppointment) {
        if (newSpecialty.getSpecialty().getId() == existingAppointment.getSpecialty().getId() && newSpecialty.getClient().getId() == existingAppointment.getClient().getId())
            throw new IllegalArgumentException("The same profile cannot schedule two services for the same day.");
    }

    //VERIFICA SE O FIM DO ATENDIMENTO NÃO EXCEDE O INÍCIO DO PRÓXIMO
    private void validateEndTimeOverlap(AppointmentEntity newAppointment, AppointmentEntity existingAppointment) {
        if (newAppointment.getEndTime().isAfter(existingAppointment.getDate().toLocalTime()) && newAppointment.getEndTime().isBefore(existingAppointment.getEndTime()))
            throw new IllegalArgumentException("The end time of the service must not occur after the start time of another service.");
    }

    //VERIFICA SE O INÍCIO DO ATENDIMENTO NÃO SOBRESCREVE O FIM DO ANTERIOR
    private void validateStartTimeOverlap(AppointmentEntity newAppointment, AppointmentEntity existingAppointment) {
        if (newAppointment.getStartTime().isAfter(existingAppointment.getDate().toLocalTime()) && newAppointment.getStartTime().isBefore(existingAppointment.getEndTime()))
            throw new IllegalArgumentException("The start time of the service must not occur before the end time of another service.");
    }

    //VERIFICA SE NÃO HÁ AGENDAMENTO DENTRO DO PERÍODO
    private void validateWithinTimePeriod(AppointmentEntity newAppointment, AppointmentEntity existingAppointment) {
        if (newAppointment.getStartTime().isBefore(existingAppointment.getDate().toLocalTime()) && newAppointment.getEndTime().isAfter(existingAppointment.getEndTime()))
            throw new IllegalArgumentException("There is another appointment scheduled within the specified time period.");
    }

    //VERIFICA SE O INÍCIO OU O FIM DO NOVO HORÁRIO COINCIDE DOM O AGENDADO
    private void validateStartOrEndConflict(AppointmentEntity newAppointment, AppointmentEntity existingAppointment) {
        if (newAppointment.getStartTime().equals(existingAppointment.getDate().toLocalTime()) || newAppointment.getEndTime().equals(existingAppointment.getEndTime()))
            throw new IllegalArgumentException("The start or end of the new schedule conflicts with the booked one.");
    }
}
