package com.jubasbackend.controller;

import com.jubasbackend.dto.BarberDTO;
import com.jubasbackend.entity.Barber;
import com.jubasbackend.service.BarberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("employee/barber")
public class BarberController {
    @Autowired
    private BarberService barberService;

    @GetMapping
    public ResponseEntity<List<BarberDTO>> findAll() {
        return ResponseEntity.ok(barberService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BarberDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(barberService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Barber> create(@RequestBody Barber barber) {
        return ResponseEntity.ok(barberService.save(barber));
    }

}
