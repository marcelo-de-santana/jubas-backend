package com.jubasbackend.api.dto.request;

public record ProfileRecoveryRequest(String email, String newPassword, String profileCpf) {
}
