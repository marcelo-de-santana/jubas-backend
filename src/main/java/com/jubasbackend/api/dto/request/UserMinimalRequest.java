package com.jubasbackend.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserMinimalRequest(@NotBlank String email, @NotBlank String password) {
}
