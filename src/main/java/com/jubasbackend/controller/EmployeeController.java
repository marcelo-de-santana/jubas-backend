package com.jubasbackend.controller;

import com.jubasbackend.dto.RequestEmployeeDTO;
import com.jubasbackend.dto.ResponseEmployeeDTO;
import com.jubasbackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<ResponseEmployeeDTO> findByProfileId(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.findByProfileId(id));
    }

    @PostMapping
    public ResponseEntity<ResponseEmployeeDTO> registerEmployee(@RequestBody RequestEmployeeDTO employee) {
        return ResponseEntity.ok(employeeService.register(employee));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseEmployeeDTO> updateEmployee(@PathVariable UUID id,
                                                                             @RequestBody RequestEmployeeDTO employee) {
        return ResponseEntity.ok(employeeService.update(id, employee));
    }
}
