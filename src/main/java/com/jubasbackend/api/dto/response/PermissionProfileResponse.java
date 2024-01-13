package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.PermissionEntity;

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
