package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Category;
import com.jubasbackend.domain.repository.CategoryRepository;
import com.jubasbackend.dto.request.CategoryRequest;
import com.jubasbackend.dto.response.CategoryMinimalResponse;
import com.jubasbackend.dto.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository repository;

    protected Category findCategoryById(Short id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Category doesn't exist."));
    }

    public List<CategoryMinimalResponse> findAll() {
        return repository.findAll().stream().map(CategoryMinimalResponse::new).toList();
    }

    public List<CategoryResponse> findAllWithSpecialty() {
        return repository.findAll().stream().map(CategoryResponse::new).toList();
    }


    public CategoryMinimalResponse create(CategoryRequest request) {
        Category newCategory = new Category(request);
        return new CategoryMinimalResponse(repository.save(newCategory));
    }

    public CategoryMinimalResponse update(Short id, CategoryRequest request) {
        Category categoryToUpdate = new Category(request);
        categoryToUpdate.setId(findCategoryById(id).getId());
        return new CategoryMinimalResponse(repository.save(categoryToUpdate));
    }

    public void delete(Short id) {
        Category categoryToDelete = findCategoryById(id);
        repository.delete(categoryToDelete);
    }
}
