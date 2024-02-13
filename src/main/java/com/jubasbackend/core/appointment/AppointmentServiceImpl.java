package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.appointment.dto.AppointmentResponse;
import com.jubasbackend.core.appointment.dto.AppointmentUpdateRequest;
import com.jubasbackend.core.appointment.dto.ScheduleResponse;
import com.jubasbackend.core.appointment.enums.AppointmentStatus;
import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.employee.EmployeeRepository;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static com.jubasbackend.utils.DateTimeUtils.getEndDay;
import static com.jubasbackend.utils.DateTimeUtils.getSelectedDate;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<ScheduleResponse> findAppointments(Optional<LocalDate> requestDate) {
        var employees = employeeRepository.findAll();
        if (employees.isEmpty())
            throw new NoSuchElementException("No employees.");

        var appointments = findAppointmentsInTheRepository(requestDate);

        if (appointments.isEmpty())
            return employees.stream().map(ScheduleResponse::new).toList();

        return employees.stream().map(employee -> new ScheduleResponse(employee, appointments)).toList();
    }

    @Override
    public AppointmentResponse findAppointment(UUID appointmentId) {
        return new AppointmentResponse(findAppointmentInTheRepository(appointmentId));
    }

    @Override
    public AppointmentEntity createAppointment(AppointmentCreateRequest request) {
        var employee = findEmployeeInTheRepository(request.employeeId());
        if (!employee.makesSpecialty(request.specialtyId()))
            throw new IllegalArgumentException("Employee doesn't makes specialty.");

        var registeredAppointments = findAppointmentsInTheRepository(request.date(), request.employeeId(), request.clientId());
        var newAppointment = new AppointmentEntity(request, employee);

        //VERIFICA SE O FUNCIONÁRIO REALIZA O SERVIÇO
        newAppointment.validateIfEmployeeMakesSpecialty();

        validateAppointment(registeredAppointments, newAppointment);

        return appointmentRepository.save(newAppointment);
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

    private List<AppointmentEntity> findAppointmentsInTheRepository(Optional<LocalDate> date) {
        var selectedDate = getSelectedDate(date);
        return appointmentRepository.findAllByDateBetween(selectedDate, getEndDay(selectedDate));
    }

    private List<AppointmentEntity> findAppointmentsInTheRepository(LocalDate date, UUID employeeId, UUID clientId) {
        var selectedDate = getSelectedDate(date);
        return appointmentRepository.findAllByDateBetweenAndEmployeeIdOrClientId(selectedDate, getEndDay(selectedDate), employeeId, clientId);
    }

    private EmployeeEntity findEmployeeInTheRepository(UUID employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new NoSuchElementException("Employee doesn't registered."));
    }

    private AppointmentEntity findAppointmentInTheRepository(UUID appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new NoSuchElementException("Appointment not found."));
    }

    private void validateAppointment(List<AppointmentEntity> registeredAppointments, AppointmentEntity requestAppointment) {
        if (!registeredAppointments.isEmpty())
            registeredAppointments.forEach(existingAppointment -> existingAppointment.validate(requestAppointment));
    }

}
