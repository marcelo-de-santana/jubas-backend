package com.jubasbackend.api.dto.response;

import com.jubasbackend.domain.entity.PermissionEntity;

public record PermissionResponse(Short id, String type) {
    public PermissionResponse(PermissionEntity permission) {
        this(permission.getId(), permission.getType());
    }
}
