package com.jubasbackend.infrastructure.repository;

import com.jubasbackend.infrastructure.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Short> {
}
