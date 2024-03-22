package com.jubasbackend.controller.request;

import com.jubasbackend.domain.entity.enums.PermissionType;
import jakarta.validation.constraints.Size;

public record UserRequest(
        String email,
        @Size(min = 8, max = 20)
        String password,
        PermissionType permission,
        String name,
        String cpf
) {
}
