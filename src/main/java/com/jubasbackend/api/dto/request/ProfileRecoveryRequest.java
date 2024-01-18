package com.jubasbackend.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfileRecoveryRequest(
        @NotNull
        @NotBlank
        @Email
        String email,

        @NotNull
        @NotBlank
        String newPassword,

        @NotNull
        @NotBlank
        String profileCpf) {
}
