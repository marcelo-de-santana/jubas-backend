package com.jubasbackend.controller;

import com.jubasbackend.domain.entity.Employee;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.WorkingHours;
import com.jubasbackend.dto.employee.EmployeeDTO;
import com.jubasbackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<EmployeeDTO> findByProfileId(@PathVariable UUID id){
        return ResponseEntity.ok(employeeService.findByProfileId(id));
    }


    @PostMapping
    public  ResponseEntity<EmployeeDTO> registerEmployee(@RequestBody Profile profile) {
        return ResponseEntity.ok(employeeService.registerEmployee(profile));
    }

    @PatchMapping("/{employeeId}/working-hours")
    public ResponseEntity<String> updateWorkingHoursForEmployee(@PathVariable UUID employeeId, @RequestBody WorkingHours workingHours){
        employeeService.updateWorkingHoursForEmployee(employeeId,workingHours);
        return ResponseEntity.ok("Horário atualizado com sucesso.");
    }
}
