package com.jubasbackend.controller;

import com.jubasbackend.dto.WorkingHoursRequestDTO;
import com.jubasbackend.dto.WorkingHoursResponseDTO;
import com.jubasbackend.service.WorkingHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/working-hours")
public class WorkingHoursController {

    @Autowired
    private WorkingHoursService workingHoursService;

    @GetMapping
    public ResponseEntity<List<WorkingHoursResponseDTO>> findAll() {
        return ResponseEntity.ok(workingHoursService.findAll());
    }

    @PostMapping
    public ResponseEntity<WorkingHoursResponseDTO> create(@RequestBody WorkingHoursRequestDTO workingHours) {
        return ResponseEntity.ok(workingHoursService.create(workingHours));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WorkingHoursResponseDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(workingHoursService.delete(id));
    }
}
