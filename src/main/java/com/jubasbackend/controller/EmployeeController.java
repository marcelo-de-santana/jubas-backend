package com.jubasbackend.controller;

import com.jubasbackend.dto.request.EmployeeRequest;
import com.jubasbackend.dto.response.EmployeeResponse;
import com.jubasbackend.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping("/profile/{id}")
    public ResponseEntity<EmployeeResponse> findByProfileId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findByProfileId(id));
    }

    @Operation(summary = "Cadastrar funcionário e jornada de trabalho.")
    @PostMapping
    public ResponseEntity<EmployeeResponse> registerEmployee(@RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable UUID id,
            @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
}
