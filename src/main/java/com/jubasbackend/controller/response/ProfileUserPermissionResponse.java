package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Profile;

import java.util.UUID;

public record ProfileUserPermissionResponse(
        UUID id,
        String name,
        String cpf,
        boolean statusProfile,
        UserResponse user
) {
    public ProfileUserPermissionResponse(Profile entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getCpf(),
                entity.isStatusProfile(),
                new UserResponse(entity.getUser())
        );
    }
}
