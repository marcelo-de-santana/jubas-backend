package com.jubasbackend.core.category;

import com.jubasbackend.core.category.dto.CategoryResponse;
import com.jubasbackend.core.category.dto.CategorySpecialtyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
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
    public ResponseEntity<CategoryResponse> createCategory(String name) {
        var categoryCreated = service.createCategory(name);
        return ResponseEntity.created(URI.create("/categories/" + categoryCreated.id())).build();
    }

    @Override
    public ResponseEntity<Void> updateCategory(Short categoryId, String name) {
        service.updateCategory(categoryId, name);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteCategory(Short categoryId) {
        service.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
