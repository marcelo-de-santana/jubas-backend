package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Employee;
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
        var profileToBeLinked = profileService.findProfileById(request.profileId());

        if (repository.existsByProfile(profileToBeLinked)) {
            throw new IllegalArgumentException("The profile is already linked to an employee.");
        }

        var employeeToCreate = new Employee(profileToBeLinked);
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
