package com.jubasbackend.service.impl;

import com.jubasbackend.domain.entity.EmployeeEntity;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.api.dto.request.EmployeeRequest;
import com.jubasbackend.api.dto.response.EmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class EmployeeServiceImpl {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private ProfileServiceImpl profileService;

    @Autowired
    private WorkingHoursServiceImpl workingHoursService;

    protected EmployeeEntity findEmployeeById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't exist."));
    }

    public EmployeeResponse findByProfileId(UUID id) {
        return new EmployeeResponse(repository.findEmployeeByProfileId(id).orElseThrow(
                () -> new NoSuchElementException("There is no profile registered for the employee")));
    }

    public EmployeeResponse register(EmployeeRequest request) {
        var profileToBeLinked = profileService.findProfileById(request.profileId());

        if (repository.existsByProfile(profileToBeLinked)) {
            throw new IllegalArgumentException("The profile is already linked to an employee.");
        }

        var employeeToCreate = new EmployeeEntity(profileToBeLinked);
        var workingHoursToBeLinked = workingHoursService.findWorkingHoursById(request.workingHourId());

        employeeToCreate.setWorkingHours(workingHoursToBeLinked);

        return new EmployeeResponse(repository.save(employeeToCreate));
    }

    public EmployeeResponse update(UUID id, EmployeeRequest request) {
        var employeeToUpdate = findEmployeeById(id);
        if(request.profileId() != null){
            var profile = profileService.findProfileById(request.profileId());
            employeeToUpdate.setProfile(profile);
        }
        if(request.workingHourId() != null){
            var workingHours = workingHoursService.findWorkingHoursById(request.workingHourId());
            employeeToUpdate.setWorkingHours(workingHours);
        }
        return new EmployeeResponse(repository.save(employeeToUpdate));
    }

}
