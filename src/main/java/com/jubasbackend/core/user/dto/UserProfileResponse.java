package com.jubasbackend.core.user.dto;

import com.jubasbackend.core.profile.dto.ProfileResponse;
import com.jubasbackend.core.user.UserEntity;
import com.jubasbackend.core.user.enums.PermissionType;

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
