package com.jubasbackend.core.permission.dto;

import com.jubasbackend.core.permission.PermissionEntity;
import com.jubasbackend.core.user.dto.UserResponse;

import java.util.List;

public record PermissionUserResponse(Short id, String type, List<UserResponse> users) {
    public PermissionUserResponse(PermissionEntity permission) {
        this(
                permission.getId(),
                permission.getType(),
                permission.getUsers().stream().map(UserResponse::new).toList());
    }
}
