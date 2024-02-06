package com.jubasbackend.core.profile;

import com.jubasbackend.core.profile.dto.*;

import java.util.List;
import java.util.UUID;

public interface ProfileService {

    List<ProfileResponse> findProfiles();

    List<ProfileUserPermissionResponse> findProfilesAndUser();

    ProfileResponse updateProfile(UUID profileId, ProfileRequest request);

    ProfileResponse createProfile(ProfileUserRequest request);

    void deleteProfile(UUID profileId);

    ProfileResponse recoveryPassword(ProfileRecoveryRequest request);

}
