package com.jubasbackend.dto.response;

import com.jubasbackend.domain.entity.User;

import java.util.UUID;

public record UserMinimalResponse(UUID id, String email) {
    public UserMinimalResponse(User user) {
        this(user.getId(), user.getEmail());
    }
}
