package com.jubasbackend.api.controller;

import com.jubasbackend.api.dto.request.WorkingHourRequest;
import com.jubasbackend.api.dto.response.WorkingHoursResponse;
import com.jubasbackend.service.impl.WorkingHoursServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/working-hours")
public class WorkingHoursController {

    private final WorkingHoursServiceImpl service;

    @GetMapping
    public ResponseEntity<List<WorkingHoursResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<WorkingHoursResponse> create(@RequestBody WorkingHourRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Working Hours has been deleted successfully");
    }
}
