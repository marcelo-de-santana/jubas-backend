package com.jubasbackend.dto.request;

public record RequestUserDTO(String email, String password, Short userPermissionId) {
}
