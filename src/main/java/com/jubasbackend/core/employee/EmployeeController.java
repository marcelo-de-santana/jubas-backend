package com.jubasbackend.core.employee;

import com.jubasbackend.core.employee.dto.EmployeeRequest;
import com.jubasbackend.core.employee.dto.EmployeeResponse;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

    private final EmployeeService service;

    @Override
    public ResponseEntity<List<EmployeeResponse>> findEmployees() {
        return ResponseEntity.ok(service.findEmployees());
    }

    @Override
    public ResponseEntity<EmployeeResponse> findEmployee(UUID employeeId) {
        return ResponseEntity.ok(service.findEmployee(employeeId));
    }

    @Override
    public ResponseEntity<List<? extends ScheduleTimeResponse>> findAppointmentsByEmployee(UUID employeeId, LocalDate date) {
        return ResponseEntity.ok(service.findAppointmentsByEmployee(employeeId, date));
    }

    @Override
    public ResponseEntity<Void> createEmployee(EmployeeRequest request) {
        var createdEmployee = service.createEmployee(request);
        return ResponseEntity.created(URI.create("/employees/" + createdEmployee.id())).build();
    }

    @Override
    public ResponseEntity<Void> updateEmployee(UUID employeeId, EmployeeRequest request) {
        service.updateEmployee(employeeId, request);
        return ResponseEntity.noContent().build();
    }


}
