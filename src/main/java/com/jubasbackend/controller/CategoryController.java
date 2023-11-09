package com.jubasbackend.controller;

import com.jubasbackend.dto.request.CategoryRequest;
import com.jubasbackend.dto.response.CategoryMinimalResponse;
import com.jubasbackend.dto.response.CategoryResponse;
import com.jubasbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryMinimalResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/specialties")
    public ResponseEntity<List<CategoryResponse>> findAllWithSpecialty() {
        return ResponseEntity.ok(service.findAllWithSpecialty());
    }

    @PostMapping
    public ResponseEntity<CategoryMinimalResponse> create(@RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryMinimalResponse> update(
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
