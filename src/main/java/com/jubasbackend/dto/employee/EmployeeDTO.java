package com.jubasbackend.dto.employee;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.EmployeeServices;
import com.jubasbackend.domain.entity.WorkingHours;
import com.jubasbackend.dto.profile.ProfileDTO;

import java.util.List;
import java.util.UUID;

public record EmployeeDTO(UUID id, ProfileDTO profile, WorkingHours workingHours, List<EmployeeServices> services) {
    public EmployeeDTO(Employee employee) {
        this(employee.getId(), new ProfileDTO(employee.getProfile()), employee.getWorkingHours(), employee.getServices());
    }
}
