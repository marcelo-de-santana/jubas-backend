package com.jubasbackend.api.controller;

import com.jubasbackend.api.ProfileApi;
import com.jubasbackend.api.dto.request.ProfileRecoveryRequest;
import com.jubasbackend.api.dto.request.ProfileRequest;
import com.jubasbackend.api.dto.request.ProfileUserRequest;
import com.jubasbackend.api.dto.response.ProfileResponse;
import com.jubasbackend.service.impl.ProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProfileController implements ProfileApi {
    private final ProfileServiceImpl service;

    @Override
    public ResponseEntity<ProfileResponse> createProfile(ProfileUserRequest request) {
        var profileCreated = service.createProfile(request);
        return ResponseEntity.created(URI.create("/profile/" + profileCreated.id())).body(profileCreated);
    }

    @Override
    public ResponseEntity<ProfileResponse> updateProfile(
            UUID profileId,
            ProfileRequest request) {
        return ResponseEntity.ok(service.updateProfile(profileId, request));
    }

    @Override
    public ResponseEntity<Void> recoveryPassword(ProfileRecoveryRequest request) {
        service.recoveryPassword(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteProfile(UUID profileId) {
        service.deleteProfile(profileId);
        return ResponseEntity.noContent().build();
    }

}
