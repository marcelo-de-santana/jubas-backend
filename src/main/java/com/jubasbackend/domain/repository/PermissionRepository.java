package com.jubasbackend.domain.repository;

import com.jubasbackend.domain.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Short> {
}
