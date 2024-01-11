package com.jubasbackend.api.dto.response;

import com.jubasbackend.domain.entity.CategoryEntity;

public record CategoryResponse(Short id, String name) {
    public CategoryResponse(CategoryEntity entity) {
        this(entity.getId(), entity.getName());
    }
}
