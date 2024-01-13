package com.jubasbackend.service.impl;

import com.jubasbackend.infrastructure.entity.SpecialtyEntity;
import com.jubasbackend.infrastructure.repository.SpecialtyRepository;
import com.jubasbackend.api.dto.request.SpecialtyRequest;
import com.jubasbackend.api.dto.response.SpecialtyCategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class SpecialtyServiceImpl {

    @Autowired
    private SpecialtyRepository repository;

    protected SpecialtyEntity findSpecialtyById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Unregistered Specialty."));
    }

    public List<SpecialtyCategoryResponse> findAll() {
        return repository.findAll().stream().map(SpecialtyCategoryResponse::new).toList();
    }

    public SpecialtyCategoryResponse create(SpecialtyRequest request) {
        SpecialtyEntity specialtyToCreate = new SpecialtyEntity(request);
        return new SpecialtyCategoryResponse(repository.save(specialtyToCreate));
    }

    public SpecialtyCategoryResponse update(UUID id, SpecialtyRequest request) {
        SpecialtyEntity specialtyToUpdate = new SpecialtyEntity(request);
        specialtyToUpdate.setId(findSpecialtyById(id).getId());
        return new SpecialtyCategoryResponse(repository.save(specialtyToUpdate));
    }

    public void delete(UUID id) {
        SpecialtyEntity specialtyToDelete = findSpecialtyById(id);
        repository.delete(specialtyToDelete);
    }
}
