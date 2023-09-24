package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.dto.profile.ProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ProfileDTO> findAllByUserId(UUID id) {
        return profileRepository.findAllByUserId(id).stream().map(ProfileDTO::new).toList();
    }

    public ProfileDTO updateUserAndProfile(Profile profile) {
        User user = userRepository.findUserByEmail(profile.getUser().getEmail()).orElseThrow(() -> new NoSuchElementException("User doesn't exists."));
        Profile newProfile = profileRepository.findById(profile.getId()).orElseThrow(() -> new NoSuchElementException("Profile doesn't exists."));
        newProfile.setName(profile.getName());
        newProfile.setCpf(profile.getCpf());
        newProfile.setStatusProfile(profile.isStatusProfile());
        newProfile.setUser(user);
        return new ProfileDTO(profileRepository.save(newProfile));
    }

    public ProfileDTO create(Profile profile) {
        return new ProfileDTO(profileRepository.save(profile));
    }

    public ProfileDTO delete(UUID id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Profile doesn't exists."));
        profileRepository.deleteById(id);
        return new ProfileDTO(profile);
    }

    public List<ProfileDTO> findAllProfilesByUserPermissionId(Short id) {
        return profileRepository.findAllByUserUserPermissionId(id).stream().map(ProfileDTO::new).toList();
    }

    public ProfileDTO updateProfile(Profile profile) {
        profileRepository.findById(profile.getId()).orElseThrow(() -> new NoSuchElementException("Profile doesn't exists."));
        return new ProfileDTO(profileRepository.save(profile));
    }
}
