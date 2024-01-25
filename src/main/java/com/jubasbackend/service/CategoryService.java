package com.jubasbackend.service;

import com.jubasbackend.api.dto.response.CategoryResponse;
import com.jubasbackend.api.dto.response.CategorySpecialtyResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findCategories();

    List<CategorySpecialtyResponse> findCategoriesAndSpecialties();

    CategoryResponse createCategory(String categoryName);

    void updateCategory(Short categoryId, String categoryName);

    void deleteCategory(Short categoryId);
}
