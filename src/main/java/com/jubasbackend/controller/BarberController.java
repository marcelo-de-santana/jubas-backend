package com.jubasbackend.controller;

import com.jubasbackend.entity.Barber;
import com.jubasbackend.service.BarberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("employee/barber")
public class BarberController {
    @Autowired
    private BarberService barberService;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return ResponseEntity.ok(barberService.findALl());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(barberService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Barber barber) {
        Barber response = barberService.save(barber);
        return ResponseEntity.ok(response);
    }

}
