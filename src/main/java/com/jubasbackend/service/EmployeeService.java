package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.WorkingHours;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.domain.repository.WorkingHoursRepository;
import com.jubasbackend.dto.employee.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private WorkingHoursRepository workingHoursRepository;

    public EmployeeDTO findByProfileId(UUID id){
        return new EmployeeDTO(employeeRepository.findEmployeeByProfileId(id).orElseThrow(
                ()-> new NoSuchElementException("There is no profile registered for the employee")));
    }

    public EmployeeDTO registerEmployee(Profile profile) {
        Profile verifiedProfile = profileRepository.findById(profile.getId()).orElseThrow(
                () -> new NoSuchElementException("Profile doesn't exist."));
        boolean linkedProfile = employeeRepository.existsByProfile(verifiedProfile);
        if (!linkedProfile) {
            Employee employee = new Employee();
            employee.setProfile(profile);
            return new EmployeeDTO(employeeRepository.save(employee));
        }
        throw new IllegalArgumentException("The employee already has a linked profile.");
    }

    public void updateWorkingHoursForEmployee(UUID employeeId, WorkingHours workingHours) {
        System.out.println(workingHours.getId());
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't exist."));
        employee.setWorkingHours(workingHours);
    }

}

