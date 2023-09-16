package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Profile;
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

    public Object updateProfile(Profile profile) {
        return new ProfileDTO(profileRepository.save(profile));
    }
}
