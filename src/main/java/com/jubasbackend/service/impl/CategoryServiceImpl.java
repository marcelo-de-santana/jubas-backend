package com.jubasbackend.service.impl;

import com.jubasbackend.api.dto.request.CategoryRequest;
import com.jubasbackend.api.dto.response.CategoryResponse;
import com.jubasbackend.api.dto.response.CategorySpecialtyResponse;
import com.jubasbackend.infrastructure.entity.CategoryEntity;
import com.jubasbackend.infrastructure.repository.CategoryRepository;
import com.jubasbackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryEntity findCategoryOnRepository(Short id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Category doesn't exists."));
    }

    @Override
    public List<CategoryResponse> findCategories() {
        return repository.findAll().stream().map(CategoryResponse::new).toList();
    }

    @Override

    public List<CategorySpecialtyResponse> findCategoriesAndSpecialties() {
        return repository.findAll().stream().map(CategorySpecialtyResponse::new).toList();
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        if (repository.existsByName(request.name()))
            throw new IllegalArgumentException("Category already exists.");

        var newCategory = CategoryEntity.builder().name(request.name()).build();
        return new CategoryResponse(repository.save(newCategory));
    }

    @Override
    public void updateCategory(Short categoryId, CategoryRequest request) {
        var categoryToUpdate = findCategoryOnRepository(categoryId);
        categoryToUpdate.setName(request.name());
        repository.save(categoryToUpdate);
    }

    @Override
    public void deleteCategory(Short categoryId) {
        var categoryToDelete = findCategoryOnRepository(categoryId);
        repository.delete(categoryToDelete);
    }
}
