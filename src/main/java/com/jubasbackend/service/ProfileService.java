package com.jubasbackend.service;

import com.jubasbackend.controller.response.ProfileUserPermissionResponse;
import com.jubasbackend.controller.request.ProfileRecoveryRequest;
import com.jubasbackend.controller.request.ProfileRequest;
import com.jubasbackend.controller.request.ProfileUserRequest;
import com.jubasbackend.controller.response.ProfileResponse;
import com.jubasbackend.domain.entity.ProfileEntity;
import com.jubasbackend.domain.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository repository;

    public List<ProfileResponse> findProfiles() {
        var profiles = repository.findAll();
        return profiles.stream().map(ProfileResponse::new).toList();
    }

    public List<ProfileUserPermissionResponse> findProfilesAndUser() {
        var profiles = repository.findAll();
        return profiles.stream().map(ProfileUserPermissionResponse::new).toList();
    }

    public ProfileResponse updateProfile(UUID profileId, ProfileRequest request) {
        ProfileEntity profileToUpdate = findProfileOnRepository(profileId);
        if (!request.name().isBlank()) {
            profileToUpdate.setName(request.name());
        }
        if (!request.cpf().isBlank()) {
            profileToUpdate.setCpf(request.cpf());
        }
        profileToUpdate.setStatusProfile(request.statusProfile());

        return new ProfileResponse(repository.save(profileToUpdate));
    }

    public ProfileResponse createProfile(ProfileUserRequest request) {
        if (!repository.existsByUserId(request.userId()))
            throw new IllegalArgumentException("User doesn't exists.");

        var newProfile = new ProfileEntity(request);
        return new ProfileResponse(repository.save(newProfile));
    }

    public void deleteProfile(UUID profileId) {
        var profileToDelete = findProfileOnRepository(profileId);
        repository.delete(profileToDelete);
    }

    public ProfileResponse recoveryPassword(ProfileRecoveryRequest request) {
        var profile = findProfileByCpfAndEmailOnRepository(request.profileCpf(), request.email());
        profile.getUser().setPassword(request.newPassword());

        return new ProfileResponse(repository.save(profile));
    }

    private ProfileEntity findProfileOnRepository(UUID profileId) {
        return repository.findById(profileId).orElseThrow(
                () -> new NoSuchElementException("Profile doesn't exists."));
    }

    private ProfileEntity findProfileByCpfAndEmailOnRepository(String cpf, String email) {
        return repository.findByCpfAndUserEmail(cpf, email).orElseThrow(
                () -> new NoSuchElementException("No profile found for the given email and CPF combination.")
        );
    }
}
