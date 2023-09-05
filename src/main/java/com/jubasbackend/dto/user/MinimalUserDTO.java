package com.jubasbackend.dto.user;

import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.UserPermission;

import java.util.UUID;

public record MinimalUserDTO(UUID id, String email, UserPermission userPermission) {
    public MinimalUserDTO(User user) {
        this(user.getId(), user.getEmail(), user.getUserPermission());
    }
}