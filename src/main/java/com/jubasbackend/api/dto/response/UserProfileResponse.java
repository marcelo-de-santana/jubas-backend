package com.jubasbackend.api.dto.response;

import com.jubasbackend.domain.entity.PermissionEntity;
import com.jubasbackend.domain.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public record UserProfileResponse(UUID id, String email, PermissionEntity permission, List<ProfileResponse> profiles) {
    public UserProfileResponse(UserEntity user) {
        this(
                user.getId(),
                user.getEmail(),
                user.getPermission(),
                user.getProfile().stream().map(ProfileResponse::new).toList()
        );
    }
}
