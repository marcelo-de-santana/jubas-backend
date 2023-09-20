package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.repository.EmployeeRepository;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProfileRepository profileRepository;

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
}
