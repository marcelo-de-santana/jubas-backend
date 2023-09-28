package com.jubasbackend.controller;

import com.jubasbackend.domain.entity.WorkingHours;
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
    public ResponseEntity<List<WorkingHours>> findAll() {
        return ResponseEntity.ok(workingHoursService.findAll());
    }

    @PostMapping
    public ResponseEntity<WorkingHours> create(@RequestBody WorkingHours workingHours){
        return ResponseEntity.ok(workingHoursService.create(workingHours));
    }
}
