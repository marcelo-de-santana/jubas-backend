package com.jubasbackend.core.user.dto;

public record UserPermissionRequest(String email, String password, Short permissionId) {
}
