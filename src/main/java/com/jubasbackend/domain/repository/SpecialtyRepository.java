package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, UUID> {
}
