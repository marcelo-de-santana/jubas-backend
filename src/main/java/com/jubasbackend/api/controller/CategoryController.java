package com.jubasbackend.api.controller;

import com.jubasbackend.api.CategoryApi;
import com.jubasbackend.api.dto.request.CategoryRequest;
import com.jubasbackend.api.dto.response.CategoryResponse;
import com.jubasbackend.api.dto.response.CategorySpecialtyResponse;
import com.jubasbackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController implements CategoryApi {
    private final CategoryService service;

    @Override
    public ResponseEntity<List<CategoryResponse>> findCategories() {
        return ResponseEntity.ok(service.findCategories());
    }

    @Override
    public ResponseEntity<List<CategorySpecialtyResponse>> findCategoriesAndSpecialties() {
        return ResponseEntity.ok(service.findCategoriesAndSpecialties());
    }

    @Override
    public ResponseEntity<CategoryResponse> createCategory(CategoryRequest request) {
        var categoryCreated = service.createCategory(request);
        return ResponseEntity.created(URI.create("/category/" + categoryCreated.id())).build();
    }

    @Override
    public ResponseEntity<Void> updateCategory(Short categoryId, CategoryRequest request) {
        service.updateCategory(categoryId, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteCategory(Short categoryId) {
        service.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
