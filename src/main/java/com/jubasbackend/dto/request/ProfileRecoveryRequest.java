package com.jubasbackend.dto.request;

public record ProfileRecoveryRequest(String email, String newPassword, String profileCpf) {
}
