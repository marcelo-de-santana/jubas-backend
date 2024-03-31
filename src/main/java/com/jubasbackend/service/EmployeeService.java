package com.jubasbackend.service;

import com.jubasbackend.controller.request.EmployeeRequest;
import com.jubasbackend.controller.response.EmployeeResponse;
import com.jubasbackend.controller.response.ScheduleTimeResponse;
import com.jubasbackend.controller.response.ScheduleTimeResponse.WithoutId;
import com.jubasbackend.domain.entity.*;
import com.jubasbackend.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.jubasbackend.utils.DateTimeUtils.obtainDateTimeFromOptionalDate;
import static com.jubasbackend.utils.DateTimeUtils.parseEndOfDay;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ProfileRepository profileRepository;
    private final WorkingHourRepository workingHourRepository;
    private final EmployeeSpecialtyRepository employeeSpecialtyRepository;
    private final AppointmentRepository appointmentRepository;

    public List<EmployeeResponse> findEmployees() {
        return employeeRepository.findAll().stream().map(EmployeeResponse::new).toList();
    }

    public EmployeeResponse findEmployee(UUID employeeId) {
        return new EmployeeResponse(getEmployee(employeeId));
    }

    public List<? extends ScheduleTimeResponse> findAppointmentsByEmployee(UUID employeeId, LocalDate requestDate) {
        //BUSCA HORÁRIOS AGENDADOS COM O FUNCIONÁRIO
        var appointments = findAppointmentsInTheRepository(requestDate, employeeId);

        //GERA OS HORÁRIOS
        var workingHours = getEmployee(employeeId).getWorkingHour();
        if (appointments.isEmpty()) {
            return workingHours.getOpeningHours().stream().map(WithoutId::new).toList();
        }

        return workingHours.getAvailableTimes(appointments);
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        var workingHour = new WorkingHour();
        var specialties = new ArrayList<EmployeeSpecialty>();

        var profile = findProfileOnRepository(request.profileId());

        if (profile.getEmployee() != null)
            throw new IllegalArgumentException("Profile ID already in use.");

        if (!request.workingHourId().toString().isBlank())
            workingHour = findWorkingHourOnRepository(request.workingHourId());

        var newEmployee = Employee.builder()
                .id(profile.getId())
                .profile(profile)
                .workingHour(workingHour)
                .specialties(specialties)
                .build();

        if (!request.specialties().isEmpty())
            newEmployee.addSpecialties(request.specialties(), employeeSpecialtyRepository);

        return new EmployeeResponse(employeeRepository.save(newEmployee));
    }

    public void updateEmployee(UUID employeeId, EmployeeRequest request) {
        var employeeToUpdate = getEmployee(employeeId);

        if (!request.specialties().isEmpty())
            employeeToUpdate.addSpecialties(request.specialties(), employeeSpecialtyRepository);

        if (request.workingHourId() != null) {
            employeeToUpdate.setWorkingHour(WorkingHour.builder()
                    .id(request.workingHourId())
                    .build());
            employeeRepository.save(employeeToUpdate);
        }

    }

    private Employee getEmployee(UUID employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't registered."));
    }

    private Profile findProfileOnRepository(UUID profileId) {
        return profileRepository.findById(profileId).orElseThrow(
                () -> new NoSuchElementException("Profile doesn't exists."));
    }

    private WorkingHour findWorkingHourOnRepository(UUID workingHourId) {
        return workingHourRepository.findById(workingHourId).orElseThrow(
                () -> new NoSuchElementException("Unregistered working hours."));
    }

    private List<Appointment> findAppointmentsInTheRepository(LocalDate date, UUID employeeId) {
        var selectedDate = obtainDateTimeFromOptionalDate(date);
        return appointmentRepository.findAllByDateBetweenAndEmployeeId(selectedDate, parseEndOfDay(selectedDate), employeeId);
    }

}
