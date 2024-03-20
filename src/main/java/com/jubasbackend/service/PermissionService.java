package com.jubasbackend.service;

import com.jubasbackend.domain.repository.ProfileRepository;
import com.jubasbackend.controller.response.ProfileResponse;
import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.controller.response.UserResponse;
import com.jubasbackend.domain.entity.enums.PermissionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {


    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public List<UserResponse> findUsersByPermission(PermissionType permission) {
        return userRepository.findAllByPermission(permission).stream().map(UserResponse::new).toList();
    }

    public List<ProfileResponse> findProfilesByPermission(PermissionType permission) {
        return profileRepository.findAllByUserPermission(permission).stream().map(ProfileResponse::new).toList();
    }

}
