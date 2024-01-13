package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public record UserPermissionProfileResponse(UUID id, String email, PermissionResponse permission,
                                            List<ProfileResponse> profiles) {
    public UserPermissionProfileResponse(UserEntity user) {
        this(
                user.getId(),
                user.getEmail(),
                new PermissionResponse(user.getPermission()),
                user.getProfile().stream().map(ProfileResponse::new).toList()
        );
    }
}
