package com.jubasbackend.core.user.dto;

import com.jubasbackend.core.permission.dto.PermissionResponse;
import com.jubasbackend.core.user.UserEntity;

import java.util.UUID;

public record UserPermissionResponse(UUID id, String email, PermissionResponse permission) {
    public UserPermissionResponse(UserEntity user) {
        this(user.getId(), user.getEmail(), new PermissionResponse(user.getPermission()));
    }
}
