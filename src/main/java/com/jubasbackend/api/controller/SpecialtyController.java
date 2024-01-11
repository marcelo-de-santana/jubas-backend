package com.jubasbackend.api.controller;

import com.jubasbackend.api.dto.request.SpecialtyRequest;
import com.jubasbackend.api.dto.response.SpecialtyCategoryResponse;
import com.jubasbackend.service.impl.SpecialtyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/specialty")
public class SpecialtyController {

    @Autowired
    private SpecialtyServiceImpl service;

    @GetMapping
    public ResponseEntity<List<SpecialtyCategoryResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<SpecialtyCategoryResponse> create(@RequestBody SpecialtyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyCategoryResponse> update(
            @RequestBody UUID id,
            @RequestBody SpecialtyRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Specialty has been deleted successfully");
    }
}
