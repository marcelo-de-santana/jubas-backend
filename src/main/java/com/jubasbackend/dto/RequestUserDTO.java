package com.jubasbackend.dto;

public record RequestUserDTO(String email, String password, Short userPermissionId) {
}
