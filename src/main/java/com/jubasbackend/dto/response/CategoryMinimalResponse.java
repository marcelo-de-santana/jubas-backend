package com.jubasbackend.dto.response;

import com.jubasbackend.domain.entity.Category;

public record CategoryMinimalResponse(Short id, String name) {
    public CategoryMinimalResponse(Category entity) {
        this(entity.getId(), entity.getName());
    }
}
