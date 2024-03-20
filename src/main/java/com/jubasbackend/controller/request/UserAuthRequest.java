package com.jubasbackend.controller.request;

import jakarta.validation.constraints.NotBlank;

public record UserAuthRequest(@NotBlank String email, @NotBlank String password) {
}
