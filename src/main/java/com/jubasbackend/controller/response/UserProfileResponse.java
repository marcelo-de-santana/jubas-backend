package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.UserEntity;
import com.jubasbackend.domain.entity.enums.PermissionType;

import java.util.List;
import java.util.UUID;

public record UserProfileResponse(UUID id, String email, PermissionType permission,
                                  List<ProfileResponse> profiles) {
    public UserProfileResponse(UserEntity user) {
        this(
                user.getId(),
                user.getEmail(),
                user.getPermission(),
                user.getProfile().stream().map(ProfileResponse::new).toList()
        );
    }
}
