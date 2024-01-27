package com.jubasbackend.core.user.dto;

import com.jubasbackend.core.user.UserEntity;

import java.util.UUID;

public record UserResponse(UUID id, String email) {
    public UserResponse(UserEntity user) {
        this(user.getId(), user.getEmail());
    }
}
