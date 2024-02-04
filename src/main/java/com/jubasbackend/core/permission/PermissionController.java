package com.jubasbackend.core.permission;

import com.jubasbackend.core.permission.dto.PermissionProfileResponse;
import com.jubasbackend.core.permission.dto.PermissionResponse;
import com.jubasbackend.core.permission.dto.PermissionUserProfileResponse;
import com.jubasbackend.core.permission.dto.PermissionUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PermissionController implements PermissionApi {

    private final PermissionService service;

    @Override
    public ResponseEntity<List<PermissionResponse>> findPermissions() {
        return ResponseEntity.ok(service.findPermissions());
    }

    public ResponseEntity<List<PermissionUserProfileResponse>> findPermissionsUsersAndProfiles() {
        return ResponseEntity.ok(service.findPermissionsUsersAndProfiles());
    }

    @Override
    public ResponseEntity<PermissionUserResponse> findUsersByPermission(Short permissionId) {
        return ResponseEntity.ok(service.findUsersByPermission(permissionId));
    }

    @Override
    public ResponseEntity<List<PermissionUserResponse>> findPermissionsAndUsers() {
        return ResponseEntity.ok(service.findPermissionsAndUsers());
    }

    @Override
    public ResponseEntity<PermissionProfileResponse> findProfilesByPermission(Short permissionId) {
        return ResponseEntity.ok(service.findProfilesByPermission(permissionId));
    }

}
