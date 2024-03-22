package com.jubasbackend.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecoveryPasswordRequest(
        @NotNull
        @Email
        String email,

        @NotNull
        @NotBlank
        String newPassword,

        @NotNull
        @NotBlank
        String profileCpf) {
}
