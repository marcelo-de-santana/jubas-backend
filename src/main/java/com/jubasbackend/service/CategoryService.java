package com.jubasbackend.service;

import com.jubasbackend.api.dto.request.CategoryRequest;
import com.jubasbackend.api.dto.response.CategoryResponse;
import com.jubasbackend.api.dto.response.CategorySpecialtyResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findCategories();

    List<CategorySpecialtyResponse> findCategoriesAndSpecialties();

    CategoryResponse createCategory(CategoryRequest request);

    void updateCategory(Short categoryId, CategoryRequest request);

    void deleteCategory(Short categoryId);
}
