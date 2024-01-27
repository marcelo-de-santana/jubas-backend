package com.jubasbackend.core.profile.dto;

import com.jubasbackend.core.profile.ProfileEntity;
import com.jubasbackend.core.user.dto.UserPermissionResponse;

import java.util.UUID;

public record ProfileUserPermissionResponse(
        UUID id,
        String name,
        String cpf,
        boolean statusProfile,
        UserPermissionResponse user
) {
    public ProfileUserPermissionResponse(ProfileEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getCpf(),
                entity.isStatusProfile(),
                new UserPermissionResponse(entity.getUser())
        );
    }
}
