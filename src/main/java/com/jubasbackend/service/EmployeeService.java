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

    public EmployeeDTO registerEmployee(Employee employee) {
        Profile profile = profileRepository.findById(employee.getProfile().getId()).orElseThrow(
                () -> new NoSuchElementException("Profile doesn't exist."));
        Employee registredEmployee = employeeRepository.findEmployeeByProfileId(profile.getId());
        if (registredEmployee == null) {
            employee.setProfile(profile);
            return new EmployeeDTO(employeeRepository.save(employee));
        }
        throw new IllegalArgumentException("Employee already registered.");
    }

    public List<EmployeeDTO> findAll() {
        return employeeRepository.findAll().stream().map(EmployeeDTO::new).toList();
    }

    public void updateWorkingHoursForEmployee(UUID employeeId, WorkingHours workingHours) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new NoSuchElementException("Employee doesn't exist."));
        WorkingHours optionalWorkingHours = workingHoursRepository.findById(workingHours.getId()).orElseThrow(
                () -> new NoSuchElementException("Working Hours doesn't exist."));
        employee.setWorkingHours(optionalWorkingHours);
        employeeRepository.save(employee);
    }

}

