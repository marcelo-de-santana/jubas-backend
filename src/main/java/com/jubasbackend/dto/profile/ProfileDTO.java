package com.jubasbackend.dto.profile;

import com.jubasbackend.domain.entity.Profile;

import java.util.UUID;

public record ProfileDTO(UUID id, String name, Long cpf, boolean statusProfile) {
    public ProfileDTO(Profile profile) {
        this(profile.getId(), profile.getName(), profile.getCpf(), profile.isStatusProfile());
    }
}
