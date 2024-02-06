package com.jubasbackend.core.user.dto;

import com.jubasbackend.core.user.enums.PermissionType;

public record UserPermissionRequest(String email, String password, PermissionType permission) {
}
