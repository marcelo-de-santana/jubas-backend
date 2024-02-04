package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.employee.EmployeeRepository;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyRepository;
import com.jubasbackend.core.workingHour.dto.ScheduleTime;
import com.jubasbackend.core.workingHour.dto.ScheduledTimeWithoutId;
import com.jubasbackend.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final EmployeeSpecialtyRepository employeeSpecialtyRepository;
    private final EmployeeRepository employeeRepository;


    @Override
    public AppointmentEntity createAppointment(AppointmentCreateRequest request) {

        var compoundEntity = findEmployeeSpecialtyInTheRepository(request.employeeId(), request.specialtyId());
        var newAppointment = new AppointmentEntity(request, compoundEntity);

        return newAppointment;
    }

    private EmployeeEntity findEmployeeInTheRepository(UUID employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't registered."));
    }

    private List<AppointmentEntity> findAppointmentsInTheRepository(AppointmentEntity appointment) {
        return appointmentRepository.findAllByDateAndEmployeeIdOrClientId(
                appointment.getDate(), appointment.getEmployee().getId(), appointment.getClient().getId());
    }

    private EmployeeSpecialtyEntity findEmployeeSpecialtyInTheRepository(UUID employeeId, UUID specialtyId) {
        return employeeSpecialtyRepository.findByEmployeeIdAndSpecialtyId(employeeId, specialtyId)
                .orElseThrow(() -> new NoSuchElementException("Employee doesn't perform the specialty."));
    }

    //VERIFICA SE O CLIENTE AGENDOU O MESMO SERVIÇO NO DIA
    private void validateSameSpecialty(AppointmentEntity newSpecialty, AppointmentEntity existingAppointment) {
        if (newSpecialty.getSpecialty().getId() == existingAppointment.getSpecialty().getId() && newSpecialty.getClient().getId() == existingAppointment.getClient().getId())
            throw new IllegalArgumentException("The same profile cannot schedule two services for the same day.");
    }

    //VERIFICA SE O FIM DO ATENDIMENTO NÃO EXCEDE O INÍCIO DO PRÓXIMO
    private void validateEndTimeOverlap(AppointmentEntity newAppointment, AppointmentEntity existingAppointment) {
        if (newAppointment.endTime().isAfter(existingAppointment.getDate().toLocalTime()) && newAppointment.endTime().isBefore(existingAppointment.endTime()))
            throw new ConflictException("The end time of the service must not occur after the start time of another service.");
    }

    //VERIFICA SE O INÍCIO DO ATENDIMENTO NÃO SOBRESCREVE O FIM DO ANTERIOR
    private void validateStartTimeOverlap(AppointmentEntity newAppointment, AppointmentEntity existingAppointment) {
        if (newAppointment.startTime().isAfter(existingAppointment.getDate().toLocalTime()) && newAppointment.startTime().isBefore(existingAppointment.endTime()))
            throw new ConflictException("The start time of the service must not occur before the end time of another service.");
    }

    //VERIFICA SE NÃO HÁ AGENDAMENTO DENTRO DO PERÍODO
    private void validateWithinTimePeriod(AppointmentEntity newAppointment, AppointmentEntity existingAppointment) {
        if (newAppointment.startTime().isBefore(existingAppointment.getDate().toLocalTime()) && newAppointment.endTime().isAfter(existingAppointment.endTime()))
            throw new ConflictException("There is another appointment scheduled within the specified time period.");
    }

    //VERIFICA SE O INÍCIO OU O FIM DO NOVO HORÁRIO COINCIDE DOM O AGENDADO
    private void validateStartOrEndConflict(AppointmentEntity newAppointment, AppointmentEntity existingAppointment) {
        if (newAppointment.startTime().equals(existingAppointment.getDate().toLocalTime()) || newAppointment.endTime().equals(existingAppointment.endTime()))
            throw new ConflictException("The start or end of the new schedule conflicts with the booked one.");
    }
}
