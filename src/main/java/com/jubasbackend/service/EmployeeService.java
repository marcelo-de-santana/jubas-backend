package com.jubasbackend.service;

import com.jubasbackend.api.dto.request.EmployeeRequest;
import com.jubasbackend.api.dto.response.EmployeeWorkingHourResponse;
import com.jubasbackend.api.dto.response.EmployeeWorkingHourSpecialtiesResponse;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    EmployeeWorkingHourSpecialtiesResponse findEmployee(UUID employeeId);

    EmployeeWorkingHourResponse createEmployee(EmployeeRequest request);

    void addSpecialties(UUID employeeId, List<UUID> newSpecialties);

    void updateWorkingHour(UUID employeeId, UUID workingHourId);

}
