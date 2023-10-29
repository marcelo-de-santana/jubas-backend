package com.jubasbackend.dto.response;

import com.jubasbackend.domain.entity.User;

import java.util.UUID;

public record ResponseMinimalUserDTO(UUID id, String email) {
    public ResponseMinimalUserDTO(User user) {
        this(user.getId(), user.getEmail());
    }
}
