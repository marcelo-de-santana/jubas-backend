package com.jubasbackend.core.user.dto;

import com.jubasbackend.core.permission.dto.PermissionResponse;
import com.jubasbackend.core.profile.dto.ProfileResponse;
import com.jubasbackend.core.user.UserEntity;

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
