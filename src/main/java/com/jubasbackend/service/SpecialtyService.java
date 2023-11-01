package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Specialty;
import com.jubasbackend.domain.repository.SpecialtyRepository;
import com.jubasbackend.dto.request.SpecialtyRequest;
import com.jubasbackend.dto.response.SpecialtyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class SpecialtyService {

    @Autowired
    private SpecialtyRepository repository;

    protected Specialty findSpecialtyById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Unregistered Specialty."));
    }

    public List<SpecialtyResponse> findAll() {
        return repository.findAll().stream().map(SpecialtyResponse::new).toList();
    }

    public SpecialtyResponse create(SpecialtyRequest request) {
        Specialty specialtyToCreate = new Specialty(request);
        return new SpecialtyResponse(repository.save(specialtyToCreate));
    }

    public SpecialtyResponse update(UUID id, SpecialtyRequest request) {
        Specialty specialtyToUpdate = new Specialty(request);
        specialtyToUpdate.setId(findSpecialtyById(id).getId());
        return new SpecialtyResponse(repository.save(specialtyToUpdate));
    }

    public void delete(UUID id) {
        Specialty specialtyToDelete = findSpecialtyById(id);
        repository.delete(specialtyToDelete);
    }
}
