package com.jubasbackend.core.profile;

import com.jubasbackend.core.profile.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;

    public ProfileEntity findProfileOnRepository(UUID profileId) {
        return repository.findById(profileId).orElseThrow(
                () -> new NoSuchElementException("Profile doesn't exists."));
    }

    public ProfileEntity findProfileByCpfAndEmailOnRepository(String cpf, String email) {
        return repository.findByCpfAndUserEmail(cpf, email).orElseThrow(
                () -> new NoSuchElementException("No profile found for the given email and CPF combination.")
        );
    }

    @Override
    public List<ProfileResponse> findProfiles() {
        var profiles = repository.findAll();
        return profiles.stream().map(ProfileResponse::new).toList();
    }

    @Override
    public List<ProfileUserPermissionResponse> findProfilesWithUserAndPermission() {
        var profiles = repository.findAll();
        return profiles.stream().map(ProfileUserPermissionResponse::new).toList();
    }

    @Override
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

    @Override
    public ProfileResponse createProfile(ProfileUserRequest request) {
        if (!repository.existsByUserId(request.userId()))
            throw new IllegalArgumentException("User doesn't exists.");

        var newProfile = new ProfileEntity(request);
        return new ProfileResponse(repository.save(newProfile));
    }

    @Override
    public void deleteProfile(UUID profileId) {
        var profileToDelete = findProfileOnRepository(profileId);
        repository.delete(profileToDelete);
    }

    @Override
    public ProfileResponse recoveryPassword(ProfileRecoveryRequest request) {
        var profile = findProfileByCpfAndEmailOnRepository(request.profileCpf(), request.email());
        profile.getUser().setPassword(request.newPassword());

        return new ProfileResponse(repository.save(profile));
    }

}
