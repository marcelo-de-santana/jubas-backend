package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.CategoryEntity;

public record CategoryResponse(Short id, String name) {
    public CategoryResponse(CategoryEntity entity) {
        this(entity.getId(), entity.getName());
    }
}
