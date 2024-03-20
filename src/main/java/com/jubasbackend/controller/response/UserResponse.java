package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.UserEntity;
import com.jubasbackend.domain.entity.enums.PermissionType;

import java.util.UUID;

public record UserResponse(UUID id, String email, PermissionType permission) {
    public UserResponse(UserEntity user) {
        this(user.getId(), user.getEmail(), user.getPermission());
    }
}
