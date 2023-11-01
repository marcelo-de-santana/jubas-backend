package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.WorkingHours;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.dto.request.EmployeeRequest;
import com.jubasbackend.dto.response.EmployeeResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private WorkingHoursService workingHoursService;

    protected Employee findEmployeeById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't exist."));
    }

    public EmployeeResponse findByProfileId(UUID id) {
        return new EmployeeResponse(repository.findEmployeeByProfileId(id).orElseThrow(
                () -> new NoSuchElementException("There is no profile registered for the employee")));
    }

    public EmployeeResponse register(EmployeeRequest request) {
        Employee employeeToCreate = new Employee();

        if (request.workingHourId() != null) {
            var workingHoursToBeLinked = workingHoursService.findWorkingHoursById(request.workingHourId());
            employeeToCreate.setWorkingHours(workingHoursToBeLinked);
        }

        Profile profileToBeLinked = profileService.findProfileById(request.profileId());

        if (repository.existsByProfile(profileToBeLinked)) {
            throw new IllegalArgumentException("The profile is already linked to an employee.");
        }

        employeeToCreate.setProfile(profileToBeLinked);
        Employee savedEmployee = repository.save(employeeToCreate);
        return new EmployeeResponse(savedEmployee);
    }

    public EmployeeResponse update(UUID id, EmployeeRequest request) {
        Employee employeeToUpdate = findEmployeeById(id);
        if(request.profileId() != null){
            employeeToUpdate.setProfile(new Profile(request.profileId()));
        }
        if(request.workingHourId() != null){
            employeeToUpdate.setWorkingHours(new WorkingHours(request.workingHourId()));

        }
        return new EmployeeResponse(repository.save(employeeToUpdate));
    }

}
