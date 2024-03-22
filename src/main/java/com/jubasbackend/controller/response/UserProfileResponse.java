package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.enums.PermissionType;

import java.util.List;
import java.util.UUID;

public interface UserProfileResponse {

    record Default(UUID id, String email, PermissionType permission) implements UserProfileResponse {
        public Default(User user) {
            this(
                    user.getId(),
                    user.getEmail(),
                    user.getPermission()
            );
        }
    }

    record WithProfiles(UUID id, String email, PermissionType permission,
                        List<ProfileResponse> profiles) implements UserProfileResponse {
        public WithProfiles(User user) {
            this(
                    user.getId(),
                    user.getEmail(),
                    user.getPermission(),
                    user.getProfiles().stream().map(ProfileResponse::new).toList()
            );
        }
    }

}
