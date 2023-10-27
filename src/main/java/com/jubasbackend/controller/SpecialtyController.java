package com.jubasbackend.controller;

import com.jubasbackend.domain.entity.Specialty;
import com.jubasbackend.dto.ResponseSpecialtyDTO;
import com.jubasbackend.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/specialty")
public class SpecialtyController {

    @Autowired
    private SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<List<Specialty>> findAll() {
        return ResponseEntity.ok(specialtyService.findAll());
    }

    @PostMapping
    public ResponseEntity<Specialty> create(@RequestBody ResponseSpecialtyDTO specialty) {
        return ResponseEntity.ok(specialtyService.create(specialty));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialty> update(@RequestBody UUID id, @RequestBody ResponseSpecialtyDTO specialty) {
        return ResponseEntity.ok(specialtyService.update(id, specialty));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Specialty> update(@RequestBody UUID id) {
        return ResponseEntity.ok(specialtyService.delete(id));
    }
}
