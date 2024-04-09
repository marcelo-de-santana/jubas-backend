package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProfileResponse {

    final UUID id;
    final String name;
    final String cpf;
    final boolean statusProfile;

    public ProfileResponse(Profile profile) {
        this(profile.getId(), profile.getName(), profile.getCpf(), profile.isStatusProfile());
    }

    @Getter
    public static class WithUser extends ProfileResponse {
        UserResponse user;

        public WithUser(Profile entity) {
            super(entity.getId(), entity.getName(), entity.getCpf(), entity.isStatusProfile());
            if (entity.getUser() != null) {
                user = new UserResponse(entity.getUser());
            }
        }
    }

}
