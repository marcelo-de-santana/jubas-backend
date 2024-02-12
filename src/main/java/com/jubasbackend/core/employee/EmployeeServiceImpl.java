package com.jubasbackend.core.employee;

import com.jubasbackend.core.appointment.AppointmentEntity;
import com.jubasbackend.core.appointment.AppointmentRepository;
import com.jubasbackend.core.employee.dto.EmployeeRequest;
import com.jubasbackend.core.employee.dto.EmployeeResponse;
import com.jubasbackend.core.employee.dto.EmployeeWithoutSpecialtiesResponse;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyId;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyRepository;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.profile.ProfileRepository;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.core.working_hour.WorkingHourEntity;
import com.jubasbackend.core.working_hour.WorkingHourRepository;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse.WithoutId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static com.jubasbackend.utils.DateTimeUtils.getEndDay;
import static com.jubasbackend.utils.DateTimeUtils.getSelectedDate;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ProfileRepository profileRepository;
    private final WorkingHourRepository workingHourRepository;
    private final EmployeeSpecialtyRepository employeeSpecialtyRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<EmployeeResponse> findEmployees() {
        return employeeRepository.findAll().stream().map(EmployeeResponse::new).toList();
    }

    @Override
    public EmployeeResponse findEmployee(UUID employeeId) {
        return new EmployeeResponse(findEmployeeInTheRepository(employeeId));
    }

    @Override
    public List<? extends ScheduleTimeResponse> findAppointmentsByEmployee(UUID employeeId, Optional<LocalDate> requestDate) {
        //BUSCA HORÁRIOS AGENDADOS COM O FUNCIONÁRIO
        var appointments = findAppointmentsInTheRepository(requestDate, employeeId);

        //GERA OS HORÁRIOS
        var workingHours = findEmployeeInTheRepository(employeeId).getWorkingHour();
        if (appointments.isEmpty()) {
            return workingHours.getOpeningHours().stream().map(WithoutId::new).toList();
        }

        return workingHours.getAvailableTimes(appointments);
    }

    @Override
    public EmployeeWithoutSpecialtiesResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsById(request.profileId())) {
            throw new IllegalArgumentException("Profile ID already in use.");
        }

        var profile = findProfileOnRepository(request.profileId());
        var workingHour = findWorkingHourOnRepository(request.workingHourId());

        var newEmployee = new EmployeeEntity(request.profileId(), profile, workingHour, new ArrayList<>());

        return new EmployeeWithoutSpecialtiesResponse(employeeRepository.save(newEmployee));
    }

    @Override
    public void addSpecialties(UUID employeeId, List<UUID> newSpecialties) {
        var currentSpecialties = findEmployeeInTheRepository(employeeId).getSpecialties();

        for (var newSpecialtyId : newSpecialties) {
            var compoundId = new EmployeeSpecialtyId(employeeId, newSpecialtyId);
            var entity = EmployeeSpecialtyEntity.builder()
                    .id(compoundId)
                    .employee(EmployeeEntity.builder().id(employeeId).build())
                    .specialty(SpecialtyEntity.builder().id(newSpecialtyId).build()).build();

            if (!currentSpecialties.contains(entity)) {
                employeeSpecialtyRepository.save(entity);
            }
        }
    }

    @Override
    public void updateWorkingHour(UUID employeeId, UUID workingHourId) {
        var employeeToUpdate = findEmployeeInTheRepository(employeeId);
        employeeToUpdate.setWorkingHour(WorkingHourEntity.builder().id(workingHourId).build());
        employeeRepository.save(employeeToUpdate);
    }

    private EmployeeEntity findEmployeeInTheRepository(UUID employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't registered."));
    }

    private ProfileEntity findProfileOnRepository(UUID profileId) {
        return profileRepository.findById(profileId).orElseThrow(
                () -> new NoSuchElementException("Profile doesn't exists."));
    }

    private WorkingHourEntity findWorkingHourOnRepository(UUID workingHourId) {
        return workingHourRepository.findById(workingHourId).orElseThrow(
                () -> new NoSuchElementException("Unregistered working hours."));
    }

    private List<AppointmentEntity> findAppointmentsInTheRepository(Optional<LocalDate> date, UUID employeeId) {
        var selectedDate = getSelectedDate(date);
        return appointmentRepository.findAllByDateBetweenAndEmployeeId(selectedDate, getEndDay(selectedDate), employeeId);
    }

}
