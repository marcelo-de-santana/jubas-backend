package com.jubasbackend.controller.dto.user;

import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.UserPermission;

import java.util.UUID;

public record UserDTO(UUID id, String email, UserPermission userPermission) {
    public UserDTO(User user) {
        this(user.getId(), user.getEmail(), user.getUserPermission());
    }
}