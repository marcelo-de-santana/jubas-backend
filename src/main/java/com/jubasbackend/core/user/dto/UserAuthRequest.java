package com.jubasbackend.core.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserAuthRequest(@NotBlank String email, @NotBlank String password) {
}
