package com.jubasbackend.service.impl;

import com.jubasbackend.domain.entity.PermissionEntity;
import com.jubasbackend.domain.repository.PermissionRepository;
import com.jubasbackend.api.dto.response.PermissionResponse;
import com.jubasbackend.api.dto.response.PermissionUserResponse;
import com.jubasbackend.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;

    public PermissionEntity findPermissionOnRepository(Short permissionId) {
        return repository.findById(permissionId)
                .orElseThrow(() -> new NoSuchElementException("Permission not found."));
    }

    public List<PermissionEntity> findPermissionsOnRepository() {
        var permissions = repository.findAll();
        if (permissions.isEmpty())
            throw new NoSuchElementException("Couldn't find any permissions.");

        return permissions;
    }

    @Override
    public PermissionUserResponse findUsersByPermissionId(Short permissionId) {
        return new PermissionUserResponse(findPermissionOnRepository(permissionId));
    }

    @Override
    public List<PermissionResponse> findPermissions() {
        var permissions = findPermissionsOnRepository();
        return permissions.stream().map(PermissionResponse::new).toList();
    }

    @Override
    public List<PermissionUserResponse> findPermissionsAndUsers() {
        var permissions = findPermissionsOnRepository();
        return permissions.stream().map(PermissionUserResponse::new).toList();
    }
}
