package com.jubasbackend.service.impl;

import com.jubasbackend.api.dto.request.SpecialtyRequest;
import com.jubasbackend.api.dto.response.SpecialtyCategoryResponse;
import com.jubasbackend.api.dto.response.SpecialtyResponse;
import com.jubasbackend.infrastructure.entity.CategoryEntity;
import com.jubasbackend.infrastructure.entity.SpecialtyEntity;
import com.jubasbackend.infrastructure.repository.SpecialtyRepository;
import com.jubasbackend.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository repository;

    public SpecialtyEntity findSpecialtyOnRepository(UUID specialtyId) {
        return repository.findById(specialtyId).orElseThrow(
                () -> new NoSuchElementException("Unregistered Specialty."));
    }

    @Override
    public List<SpecialtyResponse> findSpecialties() {
        return repository.findAll().stream().map(SpecialtyResponse::new).toList();
    }

    @Override
    public SpecialtyCategoryResponse createSpecialty(SpecialtyRequest request) {
        if (repository.existsByName(request.name())) {
            throw new IllegalArgumentException("Specialty already registered.");
        }
        var newSpecialty = new SpecialtyEntity(request);
        return new SpecialtyCategoryResponse(repository.save(newSpecialty));
    }

    @Override
    public void updateSpecialty(UUID specialtyId, SpecialtyRequest request) {
        var specialtyToUpdate = findSpecialtyOnRepository(specialtyId);
        if (!request.name().isBlank())
            specialtyToUpdate.setName(request.name());

        if (!request.price().toString().isBlank())
            specialtyToUpdate.setPrice(request.price());

        if (!request.timeDuration().toString().isBlank())
            specialtyToUpdate.setTimeDuration(request.timeDuration());

        if (!request.categoryId().toString().isBlank())
            specialtyToUpdate.setCategory(CategoryEntity.builder().id(request.categoryId()).build());

        repository.save(specialtyToUpdate);
    }

    @Override
    public void deleteSpecialty(UUID specialtyId) {
        var specialtyToDelete = findSpecialtyOnRepository(specialtyId);
        repository.delete(specialtyToDelete);
    }
}
