package com.jubasbackend.api.dto.request;

import java.util.UUID;

public record ProfileRequest(String name, String cpf, boolean statusProfile, UUID userId) {
}
