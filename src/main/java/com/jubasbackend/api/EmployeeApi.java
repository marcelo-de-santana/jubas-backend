package com.jubasbackend.api;

import com.jubasbackend.api.dto.request.EmployeeRequest;
import com.jubasbackend.api.dto.response.EmployeeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Employee")
public interface EmployeeApi {
    @GetMapping("/{employeeId}")
    ResponseEntity<EmployeeResponse> findEmployee(@PathVariable UUID employeeId);

    @Operation(summary = "Cadastrar funcionário e jornada de trabalho.")
    @PostMapping
    ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeRequest request);

    @PatchMapping("/{employeeId}")
    ResponseEntity<Void> updateEmployee(
            @PathVariable UUID employeeId,
            @RequestBody EmployeeRequest request);
}
