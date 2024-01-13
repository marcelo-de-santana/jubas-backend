package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.PermissionEntity;

import java.util.List;

public record PermissionUserResponse(Short id, String type, List<UserResponse> users) {
    public PermissionUserResponse(PermissionEntity permission) {
        this(
                permission.getId(),
                permission.getType(),
                permission.getUsers().stream().map(UserResponse::new).toList());
    }
}
