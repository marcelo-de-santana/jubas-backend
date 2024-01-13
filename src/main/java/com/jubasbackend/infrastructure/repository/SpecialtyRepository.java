package com.jubasbackend.infrastructure.repository;

import com.jubasbackend.infrastructure.entity.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, UUID> {
}
