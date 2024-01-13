package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.PermissionEntity;

public record PermissionResponse(Short id, String type) {
    public PermissionResponse(PermissionEntity permission) {
        this(permission.getId(), permission.getType());
    }
}
