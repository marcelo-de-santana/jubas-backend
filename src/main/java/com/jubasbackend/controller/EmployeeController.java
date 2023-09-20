package com.jubasbackend.controller;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.dto.EmployeeDTO;
import com.jubasbackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public  ResponseEntity<EmployeeDTO> registerEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.registerEmployee(employee));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> findAll(){
        return ResponseEntity.ok(employeeService.findAll().stream().toList());
    }
}
