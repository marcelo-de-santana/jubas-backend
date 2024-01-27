package com.jubasbackend.core.user.dto;

import com.jubasbackend.core.profile.dto.ProfileResponse;
import com.jubasbackend.core.user.UserEntity;

import java.util.List;
import java.util.UUID;

public record UserProfileResponse(UUID id, String email, List<ProfileResponse> profiles) {
    public UserProfileResponse(UserEntity user) {
        this(user.getId(), user.getEmail(), user.getProfile().stream().map(ProfileResponse::new).toList());
    }

}
