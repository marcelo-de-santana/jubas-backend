package com.jubasbackend.controller;

import com.jubasbackend.domain.entity.WorkingHours;
import com.jubasbackend.service.WorkingHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
