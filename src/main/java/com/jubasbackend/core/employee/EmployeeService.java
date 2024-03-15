package com.jubasbackend.core.employee;

import com.jubasbackend.core.employee.dto.EmployeeRequest;
import com.jubasbackend.core.employee.dto.EmployeeResponse;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    List<EmployeeResponse> findEmployees();

    EmployeeResponse findEmployee(UUID employeeId);

    List<? extends ScheduleTimeResponse> findAppointmentsByEmployee(UUID employeeId, LocalDate date);

    EmployeeResponse createEmployee(EmployeeRequest request);

    void updateEmployee(UUID employeeId, EmployeeRequest request);
}
