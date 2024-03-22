package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Category;

public record CategoryResponse(Short id, String name) {
    public CategoryResponse(Category category) {
        this(category.getId(), category.getName());
    }
}
