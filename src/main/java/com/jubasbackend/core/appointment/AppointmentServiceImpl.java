package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.appointment.dto.AppointmentResponse;
import com.jubasbackend.core.appointment.dto.AppointmentUpdateRequest;
import com.jubasbackend.core.appointment.dto.ScheduleResponse;
import com.jubasbackend.core.employee.EmployeeEntity;
import com.jubasbackend.core.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        if (!registeredAppointments.isEmpty())
            registeredAppointments.forEach(existingAppointment -> existingAppointment.compare(newAppointment));

        return appointmentRepository.save(newAppointment);
    }

    @Override
    public void updateAppointment(UUID appointmentId, AppointmentUpdateRequest request) {
        //REALIZAR VERIFICAÇÕES DE FORMA QUE NÃO GERE CONFLITO  NA AGENDA
        //DESCONSIDERAR O AGENDAMENTO ATUAL NA HORA DE SELECIONAR O OUTRO
        //LEMBRAR DE VERIFICAR, PARA NÃO GERAR CONFLITOS NA AGENDA
    }

    @Override
    public void cancelAppointment(UUID appointmentId) {
        //REGRAS
        //VERIFICAR, SE O HORÁRIO JÁ PASSOU, REMOVE, SENÃO COLOCA COMO CANCELADO
        //IMPLICAÇÃO NO CADASTRO
        //AO CLIENTE DEVE SER DADA A POSSIBILIDADE DE REAGENDAR O SERVIÇO NOVAMENTE, MESMO SENDO O MESMO DIA
        //MODIFICAR REGRA DE CADASTRO
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

}
