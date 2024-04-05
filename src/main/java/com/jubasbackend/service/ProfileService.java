package com.jubasbackend.service;

import com.jubasbackend.controller.request.ProfileRequest;
import com.jubasbackend.controller.request.RecoveryPasswordRequest;
import com.jubasbackend.controller.response.ProfileResponse;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.exception.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public List<ProfileResponse> findProfiles() {
        var profiles = profileRepository.findAll();
        return profiles.stream().map(ProfileResponse::new).toList();
    }

    public List<ProfileResponse.WithUser> findProfilesWithUser() {
        return profileRepository.findAll().stream()
                .map(ProfileResponse.WithUser::new)
                .toList();
    }

    public void updateProfile(UUID profileId, ProfileRequest request) {
        var profileToUpdate = findProfileInTheRepository(profileId);

        if (request.name() != null)
            profileToUpdate.setName(request.name());

        if (request.cpf() != null)
            profileToUpdate.setCpf(request.cpf());

        if (request.statusProfile() != null)
            profileToUpdate.setStatusProfile(request.statusProfile());

        if (request.userId() != null)
            profileToUpdate.setUser(User.builder()
                    .id(request.userId())
                    .build());

        new ProfileResponse(profileRepository.save(profileToUpdate));
    }

    public ProfileResponse createProfile(ProfileRequest request) {
        if (!userRepository.existsById(request.userId()))
            throw new APIException(HttpStatus.NOT_FOUND, "User doesn't exists.");

        return new ProfileResponse(profileRepository.save(new Profile(request)));
    }

    public void deleteProfile(UUID profileId) {
        var profileToDelete = findProfileInTheRepository(profileId);
        profileRepository.delete(profileToDelete);
    }

    public ProfileResponse recoveryPassword(RecoveryPasswordRequest request) {
        var profile = findProfileByCpfAndEmailOnRepository(request.profileCpf(), request.email());
        profile.getUser().setPassword(request.newPassword());

        return new ProfileResponse(profileRepository.save(profile));
    }

    private Profile findProfileInTheRepository(UUID profileId) {
        return profileRepository.findById(profileId).orElseThrow(
                () -> new NoSuchElementException("Profile doesn't exists."));
    }

    private Profile findProfileByCpfAndEmailOnRepository(String cpf, String email) {
        return profileRepository.findByCpfAndUserEmail(cpf, email).orElseThrow(
                () -> new NoSuchElementException("No profile found for the given email and CPF combination.")
        );
    }
}
