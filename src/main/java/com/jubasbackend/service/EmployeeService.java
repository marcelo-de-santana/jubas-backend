package com.jubasbackend.service;

import com.jubasbackend.controller.request.EmployeeRequest;
import com.jubasbackend.controller.response.EmployeeResponse;
import com.jubasbackend.controller.response.ScheduleTimeResponse;
import com.jubasbackend.controller.response.ScheduleTimeResponse.WithoutId;
import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.WorkingHour;
import com.jubasbackend.domain.entity.enums.PermissionType;
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

    public List<EmployeeResponse> findAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeResponse::new)
                .toList();
    }

    public List<EmployeeResponse> findAvailableEmployees() {
        return employeeRepository.findAll().stream()
                .filter(employee ->
                        employee
                                .getProfile()
                                .getUser()
                                .getPermission() == PermissionType.BARBEIRO)
                .map(EmployeeResponse::new).toList();
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
        var profile = findProfileOnRepository(request.profileId());

        if (profile.getEmployee() != null)
            throw new IllegalArgumentException("Profile ID already in use.");

        var workingHour = new WorkingHour();

        if (request.workingHourId() != null)
            workingHour = findWorkingHourOnRepository(request.workingHourId());

        var newEmployee = Employee.builder()
                .id(profile.getId())
                .profile(profile)
                .workingHour(workingHour)
                .specialties(new ArrayList<>())
                .build();

        var savedEmployee = employeeRepository.save(newEmployee);

        if (request.specialties() != null)
            savedEmployee.manageSpecialties(request.specialties(), employeeSpecialtyRepository);

        return new EmployeeResponse(getEmployee(savedEmployee.getId()));
    }

    public void updateEmployee(UUID employeeId, EmployeeRequest request) {
        var employeeToUpdate = getEmployee(employeeId);

        if (request.workingHourId() != null) {
            employeeToUpdate.setWorkingHour(WorkingHour.builder()
                    .id(request.workingHourId())
                    .build());

            employeeRepository.save(employeeToUpdate);
        }

        if (request.specialties() != null)
            employeeToUpdate.manageSpecialties(request.specialties(), employeeSpecialtyRepository);

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
