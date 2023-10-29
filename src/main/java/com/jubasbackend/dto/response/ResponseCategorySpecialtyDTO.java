package com.jubasbackend.dto.response;

import com.jubasbackend.domain.projection.CategorySpecialtyProjection;

import java.util.ArrayList;
import java.util.List;

public record ResponseCategorySpecialtyDTO(Short id, String name, List<ResponseMinimalSpecialtyDTO> specialties) {
    public ResponseCategorySpecialtyDTO(CategorySpecialtyProjection projection) {
        this(projection.getCategoryId(), projection.getCategoryName(), new ArrayList<>());
        specialties.add(new ResponseMinimalSpecialtyDTO(projection));
    }

    public void addSpecialty(CategorySpecialtyProjection projection) {
        specialties.add(new ResponseMinimalSpecialtyDTO(projection));
    }
}
