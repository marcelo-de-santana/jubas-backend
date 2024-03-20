package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.CategoryEntity;

public record CategoryResponse(Short id, String name) {
    public CategoryResponse(CategoryEntity category) {
        this(category.getId(), category.getName());
    }
}
