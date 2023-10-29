package com.jubasbackend.controller;

import com.jubasbackend.dto.request.RequestWorkingHoursDTO;
import com.jubasbackend.dto.response.ResponseWorkingHoursDTO;
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
    public ResponseEntity<List<ResponseWorkingHoursDTO>> findAll() {
        return ResponseEntity.ok(workingHoursService.findAll());
    }

    @PostMapping
    public ResponseEntity<ResponseWorkingHoursDTO> create(@RequestBody RequestWorkingHoursDTO workingHours) {
        return ResponseEntity.ok(workingHoursService.create(workingHours));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWorkingHoursDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok(workingHoursService.delete(id));
    }
}
