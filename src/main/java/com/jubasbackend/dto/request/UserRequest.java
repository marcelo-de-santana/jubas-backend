package com.jubasbackend.dto.request;

public record UserRequest(String email, String password, Short permissionId) {
}
