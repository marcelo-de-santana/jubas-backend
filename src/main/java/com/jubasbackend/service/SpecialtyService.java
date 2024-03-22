package com.jubasbackend.service;

import com.jubasbackend.controller.request.SpecialtyRequest;
import com.jubasbackend.controller.response.SpecialtyResponse;
import com.jubasbackend.domain.entity.Category;
import com.jubasbackend.domain.entity.Specialty;
import com.jubasbackend.domain.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SpecialtyService {

    private final SpecialtyRepository repository;

    public List<SpecialtyResponse> findSpecialties() {
        return repository.findAll().stream().map(SpecialtyResponse::new).toList();
    }

    public SpecialtyResponse createSpecialty(SpecialtyRequest request) {
        if (repository.existsByName(request.name())) {
            throw new IllegalArgumentException("Specialty already registered.");
        }
        var newSpecialty = new Specialty(request);
        return new SpecialtyResponse(repository.save(newSpecialty));
    }

    public void updateSpecialty(UUID specialtyId, SpecialtyRequest request) {
        var specialtyToUpdate = findSpecialtyOnRepository(specialtyId);
        if (request.name() != null)
            specialtyToUpdate.setName(request.name());

        if (request.price() != null)
            specialtyToUpdate.setPrice(request.price());

        if (request.timeDuration() != null)
            specialtyToUpdate.setTimeDuration(request.timeDuration());

        if (request.categoryId() != null)
            specialtyToUpdate.setCategory(Category.builder()
                    .id(request.categoryId())
                    .build());

        repository.save(specialtyToUpdate);
    }

    public void deleteSpecialty(UUID specialtyId) {
        repository.deleteById(specialtyId);
    }

    private Specialty findSpecialtyOnRepository(UUID specialtyId) {
        return repository.findById(specialtyId).orElseThrow(
                () -> new NoSuchElementException("Unregistered Specialty."));
    }
}
