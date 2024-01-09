package com.jubasbackend.dto.response;

import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.Permission;

import java.util.UUID;

public record UserResponse(UUID id, String email, Permission permission) {
    public UserResponse(User user) {
        this(user.getId(), user.getEmail(), user.getPermission());
    }
}
