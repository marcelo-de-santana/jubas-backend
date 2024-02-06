package com.jubasbackend.core.permission;

import com.jubasbackend.core.profile.dto.ProfileResponse;
import com.jubasbackend.core.user.dto.UserResponse;
import com.jubasbackend.core.user.enums.PermissionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PermissionController implements PermissionApi {

    private final PermissionService service;

    @Override
    public ResponseEntity<List<UserResponse>> findUsersByPermission(PermissionType permission) {
        return ResponseEntity.ok(service.findUsersByPermission(permission));
    }

    @Override
    public ResponseEntity<List<ProfileResponse>> findProfilesByPermission(PermissionType permission) {
        return ResponseEntity.ok(service.findProfilesByPermission(permission));
    }
}
