package com.jubasbackend.core.category;

import com.jubasbackend.core.category.dto.CategoryResponse;
import com.jubasbackend.core.category.dto.CategorySpecialtyResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findCategories();

    List<CategorySpecialtyResponse> findCategoriesAndSpecialties();

    CategoryResponse createCategory(String categoryName);

    void updateCategory(Short id, String name);

    void deleteCategory(Short categoryId);
}
