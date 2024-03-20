package com.jubasbackend.controller.request;

import com.jubasbackend.domain.entity.enums.PermissionType;

public record UserRequest(String email, String password, PermissionType permission) {
}
