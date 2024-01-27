package com.jubasbackend.core.profile.dto;

import java.util.UUID;

public record ProfileUserRequest(String name, String cpf, boolean statusProfile, UUID userId) {
}
