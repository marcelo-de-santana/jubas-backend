package com.jubasbackend.core.category;

import com.jubasbackend.core.category.dto.CategoryResponse;
import com.jubasbackend.core.category.dto.CategorySpecialtyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public List<CategoryResponse> findCategories() {
        return repository.findAll().stream().map(CategoryResponse::new).toList();
    }

    @Override
    public List<CategorySpecialtyResponse> findCategoriesAndSpecialties() {
        return repository.findAll().stream().map(CategorySpecialtyResponse::new).toList();
    }

    @Override
    public CategoryResponse createCategory(String categoryName) {
        if (repository.existsByName(categoryName))
            throw new IllegalArgumentException("Category already exists.");

        var newCategory = CategoryEntity.builder().name(categoryName).build();
        return new CategoryResponse(repository.save(newCategory));
    }

    @Override
    public void updateCategory(Short categoryId, String categoryName) {
        var categoryToUpdate = findCategoryOnRepository(categoryId);
        categoryToUpdate.setName(categoryName);
        repository.save(categoryToUpdate);
    }

    @Override
    public void deleteCategory(Short categoryId) {
        repository.deleteById(categoryId);
    }

    private CategoryEntity findCategoryOnRepository(Short id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Category doesn't exists."));
    }
}
