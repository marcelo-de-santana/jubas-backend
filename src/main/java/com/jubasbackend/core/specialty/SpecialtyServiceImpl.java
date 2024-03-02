package com.jubasbackend.core.specialty;

import com.jubasbackend.core.category.CategoryEntity;
import com.jubasbackend.core.specialty.dto.SpecialtyRequest;
import com.jubasbackend.core.specialty.dto.SpecialtyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository repository;

    @Override
    public List<SpecialtyResponse> findSpecialties() {
        return repository.findAll().stream().map(SpecialtyResponse::new).toList();
    }

    @Override
    public SpecialtyResponse createSpecialty(SpecialtyRequest request) {
        if (repository.existsByName(request.name())) {
            throw new IllegalArgumentException("Specialty already registered.");
        }
        var newSpecialty = new SpecialtyEntity(request);
        return new SpecialtyResponse(repository.save(newSpecialty));
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
        repository.deleteById(specialtyId);
    }

    private SpecialtyEntity findSpecialtyOnRepository(UUID specialtyId) {
        return repository.findById(specialtyId).orElseThrow(
                () -> new NoSuchElementException("Unregistered Specialty."));
    }
}
