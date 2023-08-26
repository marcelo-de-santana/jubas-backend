package com.jubasbackend.dto.users;

import com.jubasbackend.entity.User;
import com.jubasbackend.entity.UserPermission;

import java.util.UUID;

public record MinimalUserDTO(UUID id, String email, UserPermission userPermission) {
    public MinimalUserDTO(User user) {
        this(user.getId(), user.getEmail(), user.getUserPermission());
    }
}