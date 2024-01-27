package com.jubasbackend.core.category.dto;

import com.jubasbackend.core.category.CategoryEntity;

public record CategoryResponse(Short id, String name) {
    public CategoryResponse(CategoryEntity category) {
        this(category.getId(), category.getName());
    }
}
