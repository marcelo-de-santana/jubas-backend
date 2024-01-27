package com.jubasbackend.core.permission.dto;

import com.jubasbackend.core.permission.PermissionEntity;

public record PermissionResponse(Short id, String type) {
    public PermissionResponse(PermissionEntity permission) {
        this(permission.getId(), permission.getType());
    }
}
