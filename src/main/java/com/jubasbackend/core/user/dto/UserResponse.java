package com.jubasbackend.core.user.dto;

import com.jubasbackend.core.user.UserEntity;
import com.jubasbackend.core.user.enums.PermissionType;

import java.util.UUID;

public record UserResponse(UUID id, String email, PermissionType permission) {
    public UserResponse(UserEntity user) {
        this(user.getId(), user.getEmail(), user.getPermission());
    }
}
