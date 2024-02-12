package com.jubasbackend.core.profile.dto;

import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.user.dto.UserResponse;

import java.util.UUID;

public record ProfileUserPermissionResponse(
        UUID id,
        String name,
        String cpf,
        boolean statusProfile,
        UserResponse user
) {
    public ProfileUserPermissionResponse(ProfileEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getCpf(),
                entity.isStatusProfile(),
                new UserResponse(entity.getUser())
        );
    }
}
