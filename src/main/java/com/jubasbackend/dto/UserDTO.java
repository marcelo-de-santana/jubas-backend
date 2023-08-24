package com.jubasbackend.dto;

import com.jubasbackend.entity.User;
import com.jubasbackend.entity.UserPermission;

import java.util.UUID;

public record UserDTO(UUID id, String email, UserPermission userPermission) {
    public UserDTO(User user) {
        this(user.getId(), user.getEmail(), user.getUserPermission());
    }
}