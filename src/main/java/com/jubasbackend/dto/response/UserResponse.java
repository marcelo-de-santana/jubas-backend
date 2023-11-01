package com.jubasbackend.dto.response;

import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.UserPermission;

import java.util.UUID;

public record UserResponse(UUID id, String email, UserPermission userPermission) {
    public UserResponse(User user) {
        this(user.getId(), user.getEmail(), user.getUserPermission());
    }
}