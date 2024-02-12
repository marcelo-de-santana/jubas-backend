package com.jubasbackend.core.user.dto;

import com.jubasbackend.core.user.enums.PermissionType;

public record UserRequest(String email, String password, PermissionType permission) {
}
