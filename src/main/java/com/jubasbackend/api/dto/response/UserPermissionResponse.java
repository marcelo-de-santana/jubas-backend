package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.UserEntity;

import java.util.UUID;

public record UserPermissionResponse(UUID id, String email, PermissionResponse permission) {
    public UserPermissionResponse(UserEntity user) {
        this(user.getId(), user.getEmail(), new PermissionResponse(user.getPermission()));
    }
}
