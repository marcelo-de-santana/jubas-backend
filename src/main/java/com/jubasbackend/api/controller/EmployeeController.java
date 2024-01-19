package com.jubasbackend.api.controller;

import com.jubasbackend.api.EmployeeApi;
import com.jubasbackend.api.dto.request.EmployeeCreateRequest;
import com.jubasbackend.api.dto.request.EmployeeSpecialtyRequest;
import com.jubasbackend.api.dto.request.EmployeeWorkingHourRequest;
import com.jubasbackend.api.dto.response.EmployeeProfileWorkingHourSpecialtiesResponse;
import com.jubasbackend.api.dto.response.EmployeeSpecialtyResponse;
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
    public ResponseEntity<EmployeeProfileWorkingHourSpecialtiesResponse> findEmployee(UUID employeeId) {
        return ResponseEntity.ok(service.findEmployee(employeeId));
    }

    @Override
    public ResponseEntity<EmployeeSpecialtyResponse> findEmployeeAndSpecialties(UUID employeeId) {
        return ResponseEntity.ok(service.findEmployeeAndSpecialties(employeeId));
    }

    @Override
    public ResponseEntity<Void> createEmployee(EmployeeCreateRequest request) {
        var createdEmployee = service.createEmployee(request);
        return ResponseEntity.created(URI.create("/employee/" + createdEmployee.id())).build();
    }

    @Override
    public ResponseEntity<Void> addSpecialties(UUID employeeId, EmployeeSpecialtyRequest request) {
        service.addSpecialties(employeeId, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateWorkingHour(UUID employeeId, EmployeeWorkingHourRequest request) {
        service.updateEmployee(employeeId, request);
        return ResponseEntity.noContent().build();
    }


}
