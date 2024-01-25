package com.jubasbackend.api.controller;

import com.jubasbackend.api.EmployeeApi;
import com.jubasbackend.api.dto.request.EmployeeRequest;
import com.jubasbackend.api.dto.response.EmployeeWorkingHourSpecialtiesResponse;
import com.jubasbackend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

    private final EmployeeService service;

    @Override
    public ResponseEntity<EmployeeWorkingHourSpecialtiesResponse> findEmployee(UUID employeeId) {
        return ResponseEntity.ok(service.findEmployee(employeeId));
    }

    @Override
    public ResponseEntity<Void> createEmployee(EmployeeRequest request) {
        var createdEmployee = service.createEmployee(request);
        return ResponseEntity.created(URI.create("/employee/" + createdEmployee.id())).build();
    }

    @Override
    public ResponseEntity<Void> addSpecialties(UUID employeeId, List<UUID> specialties) {
        service.addSpecialties(employeeId, specialties);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateWorkingHour(UUID employeeId, UUID workingHourId) {
        service.updateWorkingHour(employeeId, workingHourId);
        return ResponseEntity.noContent().build();
    }


}
