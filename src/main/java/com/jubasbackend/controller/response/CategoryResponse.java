package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Category;
import com.jubasbackend.domain.entity.Specialty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryResponse {

    final Short id;
    final String name;

    public CategoryResponse(Category category) {
        this(category.getId(), category.getName());
    }

    @Getter
    public static class WithSpecialty extends CategoryResponse {
        List<SpecialtyResponse> specialties;

        public WithSpecialty(Category category) {
            super(category.getId(), category.getName());

            this.specialties = mapSpecialties(category.getSpecialties());

        }

        List<SpecialtyResponse> mapSpecialties(List<Specialty> specialties) {
            return specialties.stream()
                    .map(SpecialtyResponse::new)
                    .toList();
        }
    }
}
