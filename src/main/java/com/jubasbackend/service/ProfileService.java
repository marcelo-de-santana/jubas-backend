package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.repository.ProfileRepository;
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

    public List<ProfileDTO> findAllByUserId(UUID id) {
        return profileRepository.findAllByUserId(id).stream().map(ProfileDTO::new).toList();
    }

    public ProfileDTO updateProfile(Profile requestProfile) {
        Profile response = profileRepository.findById(requestProfile.getId()).orElseThrow(() -> new NoSuchElementException("Profile doesn't exist!"));
//        if (response.getUser().getEmail() != requestProfile.getUser().getEmail()) {
//            //CHAMA O REPOSITÓRIO DE ALTERAR E-MAIL
//        }
        return new ProfileDTO(profileRepository.save(requestProfile));
    }
}
