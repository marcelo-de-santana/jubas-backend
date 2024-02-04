package com.jubasbackend.core.permission;

import com.jubasbackend.core.permission.dto.PermissionProfileResponse;
import com.jubasbackend.core.permission.dto.PermissionResponse;
import com.jubasbackend.core.permission.dto.PermissionUserProfileResponse;
import com.jubasbackend.core.permission.dto.PermissionUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository repository;

    @Override
    public List<PermissionUserProfileResponse> findPermissionsUsersAndProfiles() {
        var permissions = repository.findAll();
        return permissions.stream().map(PermissionUserProfileResponse::new).toList();
    }

    @Override
    public PermissionUserResponse findUsersByPermission(Short permissionId) {
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

    @Override
    public PermissionProfileResponse findProfilesByPermission(Short permissionId) {
        return new PermissionProfileResponse(findPermissionOnRepository(permissionId));
    }

    private PermissionEntity findPermissionOnRepository(Short permissionId) {
        return repository.findById(permissionId)
                .orElseThrow(() -> new NoSuchElementException("Permission not found."));
    }

    private List<PermissionEntity> findPermissionsOnRepository() {
        var permissions = repository.findAll();
        if (permissions.isEmpty())
            throw new NoSuchElementException("Couldn't find any permissions.");
        return permissions;
    }
}
