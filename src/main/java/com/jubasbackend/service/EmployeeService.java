package com.jubasbackend.service;

import com.jubasbackend.api.dto.request.EmployeeRequest;
import com.jubasbackend.api.dto.response.EmployeeResponse;

import java.util.UUID;

public interface EmployeeService {

    EmployeeResponse findEmployee(UUID employeeId);

    EmployeeResponse createEmployee(EmployeeRequest request);

    void updateEmployee(UUID employeeId, EmployeeRequest request);
}
