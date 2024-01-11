package com.jubasbackend.api.dto.response;

import com.jubasbackend.domain.entity.UserEntity;

import java.util.UUID;

public record UserResponse(UUID id, String email) {
    public UserResponse(UserEntity user) {
        this(user.getId(), user.getEmail());
    }
}
