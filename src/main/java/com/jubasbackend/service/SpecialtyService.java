package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Specialty;
import com.jubasbackend.domain.repository.SpecialtyRepository;
import com.jubasbackend.dto.request.RequestSpecialtyDTO;
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

    public List<Specialty> findAll() {
        return repository.findAll();
    }

    public Specialty create(RequestSpecialtyDTO specialty) {
        Specialty specialtyToCreate = new Specialty(specialty);
        return repository.save(specialtyToCreate);
    }

    public Specialty update(UUID id, RequestSpecialtyDTO specialty) {
        Specialty specialtyToUpdate = new Specialty(specialty);
        specialtyToUpdate.setId(findSpecialtyById(id).getId());
        return repository.save(specialtyToUpdate);
    }

    public Specialty delete(UUID id) {
        Specialty specialtyToDelete = findSpecialtyById(id);
        repository.delete(specialtyToDelete);
        return specialtyToDelete;
    }
}
