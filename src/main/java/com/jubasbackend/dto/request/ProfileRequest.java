package com.jubasbackend.dto.request;

import java.util.UUID;

public record ProfileRequest(String name, String cpf, boolean statusProfile, UUID userId) {
}