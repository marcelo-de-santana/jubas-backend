package com.jubasbackend.api.controller;

import com.jubasbackend.api.dto.request.CategoryRequest;
import com.jubasbackend.api.dto.response.CategoryResponse;
import com.jubasbackend.api.dto.response.CategorySpecialtyResponse;
import com.jubasbackend.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl service;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/specialties")
    public ResponseEntity<List<CategorySpecialtyResponse>> findAllWithSpecialty() {
        return ResponseEntity.ok(service.findAllWithSpecialty());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Short id,
            @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Short id) {
        service.delete(id);
        return ResponseEntity.ok("Category has been deleted successfully.");
    }
}
