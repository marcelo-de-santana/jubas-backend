package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecialtyRepository extends JpaRepository<Specialty, UUID> {
    boolean existsByName(String specialtyName);
}
