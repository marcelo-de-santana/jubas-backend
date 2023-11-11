package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.dto.request.ProfileMinimalRequest;
import com.jubasbackend.dto.request.ProfileRequest;
import com.jubasbackend.dto.request.ProfileRecoveryRequest;
import com.jubasbackend.dto.response.ProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository repository;

    @Autowired
    private UserService userService;

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
        //Check if User exists
        userService.findUserById(request.userId());
        //Update Profile
        profileToUpdate.setName(request.name());
        profileToUpdate.setCpf(request.cpf());
        profileToUpdate.setStatusProfile(request.statusProfile());
        return new ProfileResponse(repository.save(profileToUpdate));
    }

    public ProfileResponse updateOnlyProfile(UUID id, ProfileMinimalRequest request) {
        Profile profileToUpdate = findProfileById(id);
        if (request.name() != null) {
            profileToUpdate.setName(request.name());
        }
        if (request.cpf() != null && request.cpf().length() == 11) {
            profileToUpdate.setCpf(request.cpf());
        }
        if (profileToUpdate != null) {
            profileToUpdate.setStatusProfile(request.statusProfile());
        }
        assert profileToUpdate != null;
        var updatedProfile = repository.save(profileToUpdate);
        return new ProfileResponse(updatedProfile);
    }

    public ProfileResponse create(ProfileRequest request) {
        Profile savedProfile = repository.save(new Profile(request));
        return new ProfileResponse(savedProfile);
    }

    public void delete(UUID id) {
        Profile profileToDelete = findProfileById(id);
        repository.delete(profileToDelete);
    }

    public List<ProfileResponse> findAllByUserId(UUID userId) {
        return repository.findAllByUserId(userId).stream().map(ProfileResponse::new).toList();
    }

    public List<ProfileResponse> findAllProfilesByUserPermissionId(Short permissionId) {
        return repository.findAllByUserUserPermissionId(permissionId).stream().map(ProfileResponse::new).toList();
    }

    public ProfileResponse recoveryPassword(ProfileRecoveryRequest request) {
        var profile = findProfileByCpfAndEmail(request.profileCpf(),request.email());
        profile.getUser().setPassword(request.newPassword());
        return new ProfileResponse(repository.save(profile));
    }

}
