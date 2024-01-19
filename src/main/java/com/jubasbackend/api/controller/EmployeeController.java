package com.jubasbackend.api.controller;

import com.jubasbackend.api.EmployeeApi;
import com.jubasbackend.api.dto.request.EmployeeRequest;
import com.jubasbackend.api.dto.response.EmployeeResponse;
import com.jubasbackend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {

    private final EmployeeService service;

    @Override
    public ResponseEntity<EmployeeResponse> findEmployee(UUID employeeId) {
        return ResponseEntity.ok(service.findEmployee(employeeId));
    }

    @Override
    public ResponseEntity<EmployeeResponse> createEmployee(EmployeeRequest request) {
        var createdEmployee = service.createEmployee(request);
        return ResponseEntity.created(URI.create("/employee/" + createdEmployee.id())).body(createdEmployee);
    }

    @Override
    public ResponseEntity<Void> updateEmployee(UUID employeeId, EmployeeRequest request) {
        service.updateEmployee(employeeId, request);
        return ResponseEntity.noContent().build();
    }
}
