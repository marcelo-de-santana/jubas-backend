package com.jubasbackend.core.specialty;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, UUID> {
    boolean existsByName(String specialtyName);
}
