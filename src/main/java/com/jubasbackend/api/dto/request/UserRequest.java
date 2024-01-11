package com.jubasbackend.api.dto.request;

public record UserRequest(String email, String password, Short permissionId) {
}
