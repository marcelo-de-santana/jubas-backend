package com.jubasbackend.service;

import com.jubasbackend.controller.response.EmployeeResponse;
import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.repository.AppointmentRepository;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.controller.request.EmployeeRequest;
import com.jubasbackend.domain.entity.EmployeeSpecialty;
import com.jubasbackend.domain.repository.EmployeeSpecialtyRepository;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.domain.entity.WorkingHour;
import com.jubasbackend.domain.repository.WorkingHourRepository;
import com.jubasbackend.controller.response.ScheduleTimeResponse;
import com.jubasbackend.controller.response.ScheduleTimeResponse.WithoutId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static com.jubasbackend.utils.DateTimeUtils.parseEndOfDay;
import static com.jubasbackend.utils.DateTimeUtils.obtainDateTimeFromOptionalDate;

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
        return new EmployeeResponse(findEmployeeInTheRepository(employeeId));
    }

    public List<? extends ScheduleTimeResponse> findAppointmentsByEmployee(UUID employeeId, LocalDate requestDate) {
        //BUSCA HORÁRIOS AGENDADOS COM O FUNCIONÁRIO
        var appointments = findAppointmentsInTheRepository(requestDate, employeeId);

        //GERA OS HORÁRIOS
        var workingHours = findEmployeeInTheRepository(employeeId).getWorkingHour();
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
            request.specialties().forEach(newEmployee::addSpecialty);

        return new EmployeeResponse(employeeRepository.save(newEmployee));
    }

    public void updateEmployee(UUID employeeId, EmployeeRequest request) {
        var employeeToUpdate = findEmployeeInTheRepository(employeeId);

        if (!request.profileId().toString().isBlank() && request.profileId() != employeeToUpdate.getId())
            throw new IllegalArgumentException("It's not allowed to modify the profile.");

        if (!request.workingHourId().toString().isBlank())
            employeeToUpdate.setWorkingHour(WorkingHour.builder().id(request.workingHourId()).build());

        if (!request.specialties().isEmpty())
            request.specialties().forEach(specialtyId -> {
                if (employeeToUpdate.makesSpecialty(specialtyId))
                    employeeToUpdate.removeSpecialty(specialtyId);
                else
                    employeeToUpdate.addSpecialty(specialtyId);
            });

        employeeRepository.save(employeeToUpdate);
    }

    private Employee findEmployeeInTheRepository(UUID employeeId) {
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
