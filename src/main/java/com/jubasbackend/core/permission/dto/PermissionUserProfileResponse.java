package com.jubasbackend.core.permission.dto;

import com.jubasbackend.core.permission.PermissionEntity;
import com.jubasbackend.core.user.dto.UserProfileResponse;

import java.util.List;

public record PermissionUserProfileResponse(Short id, String type, List<UserProfileResponse> users) {

    public PermissionUserProfileResponse(PermissionEntity entity) {
        this(
                entity.getId(),
                entity.getType(),
                entity.getUsers().stream().map(UserProfileResponse::new).toList()
        );
    }
}
