package com.jubasbackend.service.impl;

import com.jubasbackend.infrastructure.entity.CategoryEntity;
import com.jubasbackend.infrastructure.repository.CategoryRepository;
import com.jubasbackend.api.dto.request.CategoryRequest;
import com.jubasbackend.api.dto.response.CategoryResponse;
import com.jubasbackend.api.dto.response.CategorySpecialtyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryServiceImpl {

    @Autowired
    private CategoryRepository repository;

    protected CategoryEntity findCategoryById(Short id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Category doesn't exist."));
    }

    public List<CategoryResponse> findAll() {
        return repository.findAll().stream().map(CategoryResponse::new).toList();
    }

    public List<CategorySpecialtyResponse> findAllWithSpecialty() {
        return repository.findAll().stream().map(CategorySpecialtyResponse::new).toList();
    }


    public CategoryResponse create(CategoryRequest request) {
        CategoryEntity newCategory = new CategoryEntity(request);
        return new CategoryResponse(repository.save(newCategory));
    }

    public CategoryResponse update(Short id, CategoryRequest request) {
        CategoryEntity categoryToUpdate = new CategoryEntity(request);
        categoryToUpdate.setId(findCategoryById(id).getId());
        return new CategoryResponse(repository.save(categoryToUpdate));
    }

    public void delete(Short id) {
        CategoryEntity categoryToDelete = findCategoryById(id);
        repository.delete(categoryToDelete);
    }
}
