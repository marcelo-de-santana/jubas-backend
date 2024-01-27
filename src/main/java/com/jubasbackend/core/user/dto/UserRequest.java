package com.jubasbackend.core.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(@NotBlank String email, @NotBlank String password) {
}
