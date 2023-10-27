package com.jubasbackend.dto;

import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.UserPermission;

import java.util.UUID;

public record ResponseUserDTO(UUID id, String email, UserPermission userPermission) {
    public ResponseUserDTO(User user) {
        this(user.getId(), user.getEmail(), user.getUserPermission());
    }
}