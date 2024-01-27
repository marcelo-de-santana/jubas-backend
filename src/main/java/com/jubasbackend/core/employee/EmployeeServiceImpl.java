package com.jubasbackend.core.employee;

import com.jubasbackend.core.employee.dto.EmployeeScheduleResponse;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyEntity;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyId;
import com.jubasbackend.core.employee_specialty.EmployeeSpecialtyRepository;
import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.profile.ProfileRepository;
import com.jubasbackend.core.specialty.SpecialtyEntity;
import com.jubasbackend.core.workingHour.WorkingHourEntity;
import com.jubasbackend.core.workingHour.WorkingHourRepository;
import com.jubasbackend.core.employee.dto.EmployeeRequest;
import com.jubasbackend.core.employee.dto.EmployeeWorkingHourResponse;
import com.jubasbackend.core.employee.dto.EmployeeWorkingHourSpecialtiesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ProfileRepository profileRepository;
    private final WorkingHourRepository workingHourRepository;
    private final EmployeeSpecialtyRepository employeeSpecialtyRepository;

    public EmployeeEntity findEmployeeOnRepository(UUID employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't registered."));
    }

    public ProfileEntity findProfileOnRepository(UUID profileId) {
        return profileRepository.findById(profileId).orElseThrow(
                () -> new NoSuchElementException("Profile doesn't exists."));
    }

    public WorkingHourEntity findWorkingHourOnRepository(UUID workingHourId) {
        return workingHourRepository.findById(workingHourId).orElseThrow(
                () -> new NoSuchElementException("Unregistered working hours."));
    }


    @Override
    public EmployeeWorkingHourSpecialtiesResponse findEmployee(UUID employeeId) {
        return new EmployeeWorkingHourSpecialtiesResponse(findEmployeeOnRepository(employeeId));
    }

    @Override
    public EmployeeScheduleResponse findScheduleByEmployee(UUID employeeId) {
        return new EmployeeScheduleResponse(findEmployeeOnRepository(employeeId));
    }

    @Override
    public EmployeeWorkingHourResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsById(request.profileId())) {
            throw new IllegalArgumentException("Profile ID already in use.");
        }

        var profile = findProfileOnRepository(request.profileId());
        var workingHour = findWorkingHourOnRepository(request.workingHourId());

        var newEmployee = new EmployeeEntity(request.profileId(), profile, workingHour, new ArrayList<>());

        return new EmployeeWorkingHourResponse(employeeRepository.save(newEmployee));
    }

    @Override
    public void addSpecialties(UUID employeeId, List<UUID> newSpecialties) {
        var currentSpecialties = findEmployeeOnRepository(employeeId).getSpecialties();

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
        var employeeToUpdate = findEmployeeOnRepository(employeeId);
        employeeToUpdate.setWorkingHour(WorkingHourEntity.builder().id(workingHourId).build());
        employeeRepository.save(employeeToUpdate);
    }

}
