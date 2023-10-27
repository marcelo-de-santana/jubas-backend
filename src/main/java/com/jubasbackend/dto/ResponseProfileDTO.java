package com.jubasbackend.dto;

import com.jubasbackend.domain.entity.Profile;

import java.util.UUID;

public record ResponseProfileDTO(UUID id, String name, String cpf, boolean statusProfile) {
    public ResponseProfileDTO(Profile profile) {
        this(profile.getId(), profile.getName(), profile.getCpf(), profile.isStatusProfile());
    }
}
