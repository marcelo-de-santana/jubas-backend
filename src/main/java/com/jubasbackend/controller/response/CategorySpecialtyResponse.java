package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.CategoryEntity;

import java.util.List;

public record CategorySpecialtyResponse(Short id, String name, List<SpecialtyResponse> specialties) {
    public CategorySpecialtyResponse(CategoryEntity category) {
        this(
                category.getId(),
                category.getName(),
                category.getSpecialties().stream().map(SpecialtyResponse::new).toList()
        );
    }
}
