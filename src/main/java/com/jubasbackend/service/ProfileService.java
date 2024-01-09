package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.dto.request.ProfileMinimalRequest;
import com.jubasbackend.dto.request.ProfileRecoveryRequest;
import com.jubasbackend.dto.request.ProfileRequest;
import com.jubasbackend.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository repository;
    private final UserService userService;

    protected Profile findProfileById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Profile doesn't exists."));
    }

    protected Profile findProfileByCpfAndEmail(String cpf, String email) {
        return repository.findByCpfAndUserEmail(cpf, email).orElseThrow(
                () -> new NoSuchElementException("No profile found for the given email and CPF combination.")
        );
    }

    public ProfileResponse update(UUID id, ProfileRequest request) {
        Profile profileToUpdate = findProfileById(id);
        profileToUpdate.setUser(userService.findUserByIdOnRepository(request.userId()));
        return new ProfileResponse(repository.save(profileToUpdate));
    }

    public ProfileResponse updateOnlyProfile(UUID id, ProfileMinimalRequest request) {
        Profile profileToUpdate = findProfileById(id);
        if (!request.name().isBlank()) {
            profileToUpdate.setName(request.name());
        }
        if (!request.cpf().isBlank()) {
            profileToUpdate.setCpf(request.cpf());
        }
        profileToUpdate.setStatusProfile(request.statusProfile());

        return new ProfileResponse(repository.save(profileToUpdate));
    }

    public ProfileResponse create(ProfileRequest request) {
        var newProfile = new Profile(request);
        return new ProfileResponse(repository.save(newProfile));
    }

    public void delete(UUID id) {
        Profile profileToDelete = findProfileById(id);
        repository.delete(profileToDelete);
    }

    public List<ProfileResponse> findAllByUserId(UUID userId) {
        return repository.findAllByUserId(userId).stream().map(ProfileResponse::new).toList();
    }

    public List<ProfileResponse> findAllProfilesByUserPermissionId(Short permissionId) {
        return repository.findAllByUserPermissionId(permissionId).stream().map(ProfileResponse::new).toList();
    }

    public ProfileResponse recoveryPassword(ProfileRecoveryRequest request) {
        var profile = findProfileByCpfAndEmail(request.profileCpf(),request.email());
        profile.getUser().setPassword(request.newPassword());

        return new ProfileResponse(repository.save(profile));
    }

}
