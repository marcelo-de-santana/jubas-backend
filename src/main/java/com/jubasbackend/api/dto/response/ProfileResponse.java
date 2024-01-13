package com.jubasbackend.api.dto.response;

import com.jubasbackend.infrastructure.entity.ProfileEntity;

import java.util.UUID;

public record ProfileResponse(UUID id, String name, String cpf, boolean statusProfile) {
    public ProfileResponse(ProfileEntity profile) {
        this(
                profile.getId(),
                profile.getName(),
                profile.getCpf(),
                profile.isStatusProfile());
    }

}
