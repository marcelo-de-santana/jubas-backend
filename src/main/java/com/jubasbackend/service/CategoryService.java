package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Category;
import com.jubasbackend.domain.repository.CategoryRepository;
import com.jubasbackend.dto.RequestCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    protected Category findCategoryById(Short id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Category doesn't exist."));
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category create(RequestCategoryDTO category) {
        Category newCategory = new Category(category);
        return categoryRepository.save(newCategory);
    }

    public Category update(Short id, RequestCategoryDTO category) {
        Category existingCategory = findCategoryById(id);
        existingCategory.setName(category.name());
        return categoryRepository.save(existingCategory);
    }

    public Category delete(Short id) {
        Category categoryToDelete = findCategoryById(id);
        categoryRepository.delete(categoryToDelete);
        return categoryToDelete;
    }
}
