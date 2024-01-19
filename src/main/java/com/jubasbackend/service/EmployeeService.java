package com.jubasbackend.service;

import com.jubasbackend.api.dto.request.EmployeeCreateRequest;
import com.jubasbackend.api.dto.request.EmployeeSpecialtyRequest;
import com.jubasbackend.api.dto.request.EmployeeWorkingHourRequest;
import com.jubasbackend.api.dto.response.EmployeeProfileWorkingHourSpecialtiesResponse;
import com.jubasbackend.api.dto.response.EmployeeSpecialtyResponse;

import java.util.UUID;

public interface EmployeeService {

    EmployeeProfileWorkingHourSpecialtiesResponse findEmployee(UUID employeeId);

    EmployeeProfileWorkingHourSpecialtiesResponse createEmployee(EmployeeCreateRequest request);

    EmployeeSpecialtyResponse findEmployeeAndSpecialties(UUID employeeId);

    void addSpecialties(UUID employeeId, EmployeeSpecialtyRequest request);

    void updateEmployee(UUID employeeId, EmployeeWorkingHourRequest request);



}
