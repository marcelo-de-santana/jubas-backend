package com.jubasbackend.core.employee;

import com.jubasbackend.core.employee.dto.EmployeeRequest;
import com.jubasbackend.core.employee.dto.EmployeeWorkingHourResponse;
import com.jubasbackend.core.employee.dto.EmployeeWorkingHourSpecialtiesResponse;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    EmployeeWorkingHourSpecialtiesResponse findEmployee(UUID employeeId);

    EmployeeWorkingHourResponse createEmployee(EmployeeRequest request);

    void addSpecialties(UUID employeeId, List<UUID> newSpecialties);

    void updateWorkingHour(UUID employeeId, UUID workingHourId);

}
