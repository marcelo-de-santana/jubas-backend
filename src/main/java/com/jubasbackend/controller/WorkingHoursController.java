package com.jubasbackend.controller;

import com.jubasbackend.dto.request.WorkingHoursRequest;
import com.jubasbackend.dto.response.WorkingHoursResponse;
import com.jubasbackend.service.WorkingHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/working-hours")
public class WorkingHoursController {

    @Autowired
    private WorkingHoursService service;

    @GetMapping
    public ResponseEntity<List<WorkingHoursResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<WorkingHoursResponse> create(@RequestBody WorkingHoursRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Working Hours has been deleted successfully");
    }
}
