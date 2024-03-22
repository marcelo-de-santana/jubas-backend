package com.jubasbackend.controller.response;

import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.enums.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserResponse {

    final UUID id;
    final String email;
    final PermissionType permission;

    public UserResponse(User user) {
        this(user.getId(), user.getEmail(), user.getPermission());
    }

    @Getter
    public static class WithProfiles extends UserResponse {
        List<ProfileResponse> profiles;

        public WithProfiles(User user) {
            super(user.getId(), user.getEmail(), user.getPermission());
            profiles = mapProfiles(user.getProfiles());
        }

        List<ProfileResponse> mapProfiles(List<Profile> profiles) {
            return profiles.stream()
                    .map(ProfileResponse::new)
                    .toList();
        }
    }
}
