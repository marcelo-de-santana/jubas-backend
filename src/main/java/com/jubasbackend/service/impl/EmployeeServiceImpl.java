package com.jubasbackend.service.impl;

import com.jubasbackend.api.dto.request.EmployeeCreateRequest;
import com.jubasbackend.api.dto.request.EmployeeSpecialtyRequest;
import com.jubasbackend.api.dto.request.EmployeeWorkingHourRequest;
import com.jubasbackend.api.dto.response.EmployeeProfileWorkingHourSpecialtiesResponse;
import com.jubasbackend.api.dto.response.EmployeeSpecialtyResponse;
import com.jubasbackend.infrastructure.entity.EmployeeEntity;
import com.jubasbackend.infrastructure.repository.EmployeeRepository;
import com.jubasbackend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeEntity findEmployeeOnRepository(UUID employeeId) {
        return repository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't registered."));
    }

    @Override
    public EmployeeProfileWorkingHourSpecialtiesResponse findEmployee(UUID employeeId) {
        return new EmployeeProfileWorkingHourSpecialtiesResponse(findEmployeeOnRepository(employeeId));
    }

    @Override
    public EmployeeProfileWorkingHourSpecialtiesResponse createEmployee(EmployeeCreateRequest request) {
        if (repository.existsById(request.profileId())) {
            throw new IllegalArgumentException("Profile ID already in use.");
        }
        var newEmployee = new EmployeeEntity(request);
        return new EmployeeProfileWorkingHourSpecialtiesResponse(repository.save(newEmployee));
    }

    @Override
    public EmployeeSpecialtyResponse findEmployeeAndSpecialties(UUID employeeId) {
        var employee = findEmployeeOnRepository(employeeId);
        return new EmployeeSpecialtyResponse(employee);
    }

    @Override
    public void addSpecialties(UUID employeeId, EmployeeSpecialtyRequest request) {

    }

    @Override
    public void updateEmployee(UUID employeeId, EmployeeWorkingHourRequest request) {
    }


//
//    protected EmployeeEntity findEmployeeById(UUID id) {
//        return repository.findById(id).orElseThrow(
//                () -> new NoSuchElementException("Employee doesn't exist."));
//    }
//


//
//    public EmployeeResponse register(EmployeeRequest request) {
//        var profileToBeLinked = profileService.findProfileById(request.profileId());
//
//        if (repository.existsByProfile(profileToBeLinked)) {
//            throw new IllegalArgumentException("The profile is already linked to an employee.");
//        }
//
//        var employeeToCreate = new EmployeeEntity(profileToBeLinked);
//        var workingHoursToBeLinked = workingHoursService.findWorkingHoursById(request.workingHourId());
//
//        employeeToCreate.setWorkingHours(workingHoursToBeLinked);
//
//        return new EmployeeResponse(repository.save(employeeToCreate));
//    }
//
//    public EmployeeResponse update(UUID id, EmployeeRequest request) {
//        var employeeToUpdate = findEmployeeById(id);
//        if(request.profileId() != null){
//            var profile = profileService.findProfileById(request.profileId());
//            employeeToUpdate.setProfile(profile);
//        }
//        if(request.workingHourId() != null){
//            var workingHours = workingHoursService.findWorkingHoursById(request.workingHourId());
//            employeeToUpdate.setWorkingHours(workingHours);
//        }
//        return new EmployeeResponse(repository.save(employeeToUpdate));
//    }

}
