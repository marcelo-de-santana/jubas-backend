package com.jubasbackend.dto;

import java.util.UUID;

public record RequestProfileDTO(String name, String cpf, boolean statusProfile, UUID userId) {
}