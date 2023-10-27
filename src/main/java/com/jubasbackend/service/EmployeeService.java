package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.WorkingHours;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.dto.RequestEmployeeDTO;
import com.jubasbackend.dto.ResponseEmployeeDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private WorkingHoursService workingHoursService;

    protected Employee findEmployeeById(UUID id) {
        return employeeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't exist."));
    }

    public ResponseEmployeeDTO findByProfileId(UUID id) {
        return new ResponseEmployeeDTO(employeeRepository.findEmployeeByProfileId(id).orElseThrow(
                () -> new NoSuchElementException("There is no profile registered for the employee")));
    }

    public ResponseEmployeeDTO register(RequestEmployeeDTO employee) {
        Employee employeeToCreate = new Employee();

        if (employee.workingHourId() != null) {
            var workingHoursToBeLinked = workingHoursService.findWorkingHoursById(employee.workingHourId());
            employeeToCreate.setWorkingHours(workingHoursToBeLinked);
        }

        Profile profileToBeLinked = profileService.findProfileById(employee.profileId());

        if (employeeRepository.existsByProfile(profileToBeLinked)) {
            throw new IllegalArgumentException("The profile is already linked to an employee.");
        }

        employeeToCreate.setProfile(profileToBeLinked);
        Employee savedEmployee = employeeRepository.save(employeeToCreate);
        return new ResponseEmployeeDTO(savedEmployee);
    }

    public ResponseEmployeeDTO update(UUID id, RequestEmployeeDTO employee) {
        Employee employeeToUpdate = findEmployeeById(id);
        if(employee.profileId() != null){
            employeeToUpdate.setProfile(new Profile(employee.profileId()));
        }
        if(employee.workingHourId() != null){
            employeeToUpdate.setWorkingHours(new WorkingHours(employee.workingHourId()));

        }
        return new ResponseEmployeeDTO(employeeRepository.save(employeeToUpdate));
    }

}
