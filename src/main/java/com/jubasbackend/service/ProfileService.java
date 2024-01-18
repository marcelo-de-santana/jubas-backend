package com.jubasbackend.service;

import com.jubasbackend.api.dto.request.ProfileRecoveryRequest;
import com.jubasbackend.api.dto.request.ProfileRequest;
import com.jubasbackend.api.dto.request.ProfileUserRequest;
import com.jubasbackend.api.dto.response.ProfileResponse;

import java.util.UUID;

public interface ProfileService {
    ProfileResponse updateProfile(UUID profileId, ProfileRequest request);

    ProfileResponse createProfile(ProfileUserRequest request);

    void deleteProfile(UUID profileId);

    ProfileResponse recoveryPassword(ProfileRecoveryRequest request);
}
