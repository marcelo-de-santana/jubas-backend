package com.jubasbackend.dto.response;

import com.jubasbackend.domain.entity.Permission;
import com.jubasbackend.domain.entity.User;

import java.util.List;
import java.util.UUID;

public record UserProfileResponse(UUID id, String email, Permission permission, List<ProfileResponse> profiles) {
    public UserProfileResponse(User user) {
        this(
                user.getId(),
                user.getEmail(),
                user.getPermission(),
                user.getProfile().stream().map(ProfileResponse::new).toList()
        );
    }
}
