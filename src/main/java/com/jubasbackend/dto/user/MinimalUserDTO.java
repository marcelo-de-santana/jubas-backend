package com.jubasbackend.dto.user;

import com.jubasbackend.domain.entity.User;

import java.util.UUID;

public record MinimalUserDTO(UUID id, String email) {
    public MinimalUserDTO(User user) {
        this(user.getId(), user.getEmail());
    }
}
