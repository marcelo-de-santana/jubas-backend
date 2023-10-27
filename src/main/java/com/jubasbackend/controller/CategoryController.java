package com.jubasbackend.controller;

import com.jubasbackend.domain.entity.Category;
import com.jubasbackend.dto.RequestCategoryDTO;
import com.jubasbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody RequestCategoryDTO category) {
        return ResponseEntity.ok(categoryService.create(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Short id, @RequestBody RequestCategoryDTO category) {
        return ResponseEntity.ok(categoryService.update(id, category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> delete(@PathVariable Short id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }
}
