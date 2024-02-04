package com.jubasbackend.core.employee;

import com.jubasbackend.core.employee.dto.EmployeeRequest;
import com.jubasbackend.core.employee.dto.EmployeeWorkingHourResponse;
import com.jubasbackend.core.employee.dto.EmployeeWorkingHourSpecialtiesResponse;
import com.jubasbackend.core.workingHour.dto.ScheduleTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeService {

    EmployeeWorkingHourSpecialtiesResponse findEmployee(UUID employeeId);

    List<? extends ScheduleTime> findAppointmentsByEmployee(UUID employeeId, Optional<LocalDate> date);

    EmployeeWorkingHourResponse createEmployee(EmployeeRequest request);

    void addSpecialties(UUID employeeId, List<UUID> newSpecialties);

    void updateWorkingHour(UUID employeeId, UUID workingHourId);

}
