package com.jubasbackend.service;

import com.jubasbackend.controller.response.CategoryResponse;
import com.jubasbackend.controller.response.CategorySpecialtyResponse;
import com.jubasbackend.domain.entity.CategoryEntity;
import com.jubasbackend.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository repository;

    public List<CategoryResponse> findCategories() {
        return repository.findAll().stream().map(CategoryResponse::new).toList();
    }

    public List<CategorySpecialtyResponse> findCategoriesAndSpecialties() {
        return repository.findAll().stream().map(CategorySpecialtyResponse::new).toList();
    }

    public CategoryResponse createCategory(String categoryName) {
        if (repository.existsByName(categoryName))
            throw new IllegalArgumentException("Category already exists.");

        var newCategory = CategoryEntity.builder().name(categoryName).build();
        return new CategoryResponse(repository.save(newCategory));
    }

    public void updateCategory(Short id, String name) {
        var categoryToUpdate = findCategoryOnRepository(id);
        categoryToUpdate.setName(name);
        repository.save(categoryToUpdate);
    }

    public void deleteCategory(Short categoryId) {
        repository.deleteById(categoryId);
    }

    private CategoryEntity findCategoryOnRepository(Short id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Category doesn't exists."));
    }
}
