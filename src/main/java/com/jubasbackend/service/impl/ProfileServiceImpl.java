package com.jubasbackend.service.impl;

import com.jubasbackend.api.dto.request.ProfileRecoveryRequest;
import com.jubasbackend.api.dto.request.ProfileRequest;
import com.jubasbackend.api.dto.request.ProfileUserRequest;
import com.jubasbackend.api.dto.response.ProfileResponse;
import com.jubasbackend.infrastructure.entity.ProfileEntity;
import com.jubasbackend.infrastructure.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;

    public ProfileEntity findProfileById(UUID profileId) {
        return repository.findById(profileId).orElseThrow(
                () -> new NoSuchElementException("Profile doesn't exists."));
    }

    public ProfileEntity findProfileByCpfAndEmailOnRepository(String cpf, String email) {
        return repository.findByCpfAndUserEmail(cpf, email).orElseThrow(
                () -> new NoSuchElementException("No profile found for the given email and CPF combination.")
        );
    }

    @Override
    public ProfileResponse updateProfile(UUID profileId, ProfileRequest request) {
        ProfileEntity profileToUpdate = findProfileById(profileId);
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
    public void delete(UUID profileId) {
        ProfileEntity profileToDelete = findProfileById(profileId);
        repository.delete(profileToDelete);
    }

    @Override
    public ProfileResponse recoveryPassword(ProfileRecoveryRequest request) {
        var profile = findProfileByCpfAndEmailOnRepository(request.profileCpf(), request.email());
        profile.getUser().setPassword(request.newPassword());

        return new ProfileResponse(repository.save(profile));
    }

}
