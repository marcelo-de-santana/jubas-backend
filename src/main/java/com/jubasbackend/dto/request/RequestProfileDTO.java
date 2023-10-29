package com.jubasbackend.dto.request;

import java.util.UUID;

public record RequestProfileDTO(String name, String cpf, boolean statusProfile, UUID userId) {
}