package com.jubasbackend.controller;

import com.jubasbackend.domain.entity.Category;
import com.jubasbackend.dto.request.RequestCategoryDTO;
import com.jubasbackend.dto.response.ResponseCategorySpecialtyDTO;
import com.jubasbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/specialties")
    public ResponseEntity<List<ResponseCategorySpecialtyDTO>> findAllWithSpecialty() {
        return ResponseEntity.ok(service.findAllWithSpecialty());
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody RequestCategoryDTO category) {
        return ResponseEntity.ok(service.create(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Short id, @RequestBody RequestCategoryDTO category) {
        return ResponseEntity.ok(service.update(id, category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> delete(@PathVariable Short id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
