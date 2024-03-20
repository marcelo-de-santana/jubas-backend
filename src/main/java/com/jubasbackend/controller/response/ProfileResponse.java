package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.ProfileEntity;

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
