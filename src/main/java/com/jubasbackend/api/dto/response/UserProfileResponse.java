package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public record UserProfileResponse(UUID id, String email, List<ProfileResponse> profiles) {
    public UserProfileResponse(UserEntity user) {
        this(user.getId(), user.getEmail(), user.getProfile().stream().map(ProfileResponse::new).toList());
    }

}
