package com.jubasbackend.core.profile.dto;

import com.jubasbackend.core.profile.ProfileEntity;

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
