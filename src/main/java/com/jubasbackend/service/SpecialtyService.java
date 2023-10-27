package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Category;
import com.jubasbackend.domain.entity.Specialty;
import com.jubasbackend.domain.repository.SpecialtyRepository;
import com.jubasbackend.dto.ResponseSpecialtyDTO;
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

    public Specialty create(ResponseSpecialtyDTO specialty) {
        Specialty specialtyToCreate = new Specialty(specialty);
        return repository.save(specialtyToCreate);
    }

    public Specialty update(UUID id, ResponseSpecialtyDTO specialty) {
        Specialty specialtyToUpdate = findSpecialtyById(id);
        specialtyToUpdate.setName(specialty.name());
        specialtyToUpdate.setTimeDuration(specialty.timeDuration());
        specialtyToUpdate.setCategory(new Category(specialty.categoryId()));
        return repository.save(specialtyToUpdate);
    }

    public Specialty delete(UUID id) {
        Specialty specialtyToDelete = findSpecialtyById(id);
        repository.delete(specialtyToDelete);
        return specialtyToDelete;
    }
}
