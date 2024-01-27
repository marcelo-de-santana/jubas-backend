package com.jubasbackend.core.profile;

import com.jubasbackend.core.profile.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProfileController implements ProfileApi {
    private final ProfileService service;

    @Override
    public ResponseEntity<List<ProfileResponse>> findProfiles() {
        return ResponseEntity.ok(service.findProfiles());
    }

    @Override
    public ResponseEntity<List<ProfileUserPermissionResponse>> findProfilesWithUserAndPermission() {
        return ResponseEntity.ok(service.findProfilesWithUserAndPermission());
    }

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
