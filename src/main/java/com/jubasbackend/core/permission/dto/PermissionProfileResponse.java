package com.jubasbackend.core.permission.dto;

import com.jubasbackend.core.permission.PermissionEntity;
import com.jubasbackend.core.profile.dto.ProfileResponse;

import java.util.List;

public record PermissionProfileResponse(Short id, String type, List<ProfileResponse> profiles) {
    public PermissionProfileResponse(PermissionEntity permission) {
        this(
                permission.getId(),
                permission.getType(),
                permission.getUsers().stream().flatMap(
                        user -> user.getProfile().stream()).map(ProfileResponse::new).toList());
    }
}
