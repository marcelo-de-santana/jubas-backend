package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.dto.RequestMinimalProfileDTO;
import com.jubasbackend.dto.RequestProfileDTO;
import com.jubasbackend.dto.ResponseProfileDTO;

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
    private UserService userService;

    protected Profile findProfileById(UUID id) {
        return profileRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Profile doesn't exists."));
    }

    public List<ResponseProfileDTO> findAllByUserId(UUID id) {
        return profileRepository.findAllByUserId(id).stream().map(ResponseProfileDTO::new).toList();
    }

    public ResponseProfileDTO update(UUID id, RequestProfileDTO profile) {
        Profile profileToUpdate = findProfileById(id);
        //Check if User exists
        userService.findUserById(profile.userId());
        //Update Profile
        profileToUpdate.setName(profile.name());
        profileToUpdate.setCpf(profile.cpf());
        profileToUpdate.setStatusProfile(profile.statusProfile());
        return new ResponseProfileDTO(profileRepository.save(profileToUpdate));
    }

    public ResponseProfileDTO updateOnlyProfile(UUID id, RequestMinimalProfileDTO profile) {
        Profile profileToUpdate = findProfileById(id);
        if (profile.name() != null) {
            profileToUpdate.setName(profile.name());
        }
        if (profile.cpf() != null && profile.cpf().length() == 11) {
            profileToUpdate.setCpf(profile.cpf());
        }
        if (profileToUpdate != null) {
            profileToUpdate.setStatusProfile(profile.statusProfile());
        }
        assert profileToUpdate != null;
        var updatedProfile = profileRepository.save(profileToUpdate);
        return new ResponseProfileDTO(updatedProfile);
    }

    public ResponseProfileDTO create(RequestProfileDTO profile) {
        Profile savedProfile = profileRepository.save(new Profile(profile));
        return new ResponseProfileDTO(savedProfile);
    }

    public ResponseProfileDTO delete(UUID id) {
        Profile profileToDelete = findProfileById(id);
        profileRepository.delete(profileToDelete);
        return new ResponseProfileDTO(profileToDelete);
    }

    public List<ResponseProfileDTO> findAllProfilesByUserPermissionId(Short id) {
        return profileRepository.findAllByUserUserPermissionId(id).stream().map(ResponseProfileDTO::new).toList();
    }


}
