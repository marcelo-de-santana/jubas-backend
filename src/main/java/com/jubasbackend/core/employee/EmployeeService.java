package com.jubasbackend.core.employee;

import com.jubasbackend.core.employee.dto.EmployeeRequest;
import com.jubasbackend.core.employee.dto.EmployeeWithoutSpecialtiesResponse;
import com.jubasbackend.core.employee.dto.EmployeeResponse;
import com.jubasbackend.core.workingHour.dto.ScheduleTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeService {

    List<EmployeeResponse> findEmployees();

    EmployeeResponse findEmployee(UUID employeeId);

    List<? extends ScheduleTime> findAppointmentsByEmployee(UUID employeeId, Optional<LocalDate> date);

    EmployeeWithoutSpecialtiesResponse createEmployee(EmployeeRequest request);

    void addSpecialties(UUID employeeId, List<UUID> newSpecialties);

    void updateWorkingHour(UUID employeeId, UUID workingHourId);
}
