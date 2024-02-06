package com.jubasbackend.core.permission;

import com.jubasbackend.core.profile.ProfileRepository;
import com.jubasbackend.core.profile.dto.ProfileResponse;
import com.jubasbackend.core.user.UserRepository;
import com.jubasbackend.core.user.dto.UserResponse;
import com.jubasbackend.core.user.enums.PermissionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;


    @Override
    public List<UserResponse> findUsersByPermission(PermissionType permission) {
        return userRepository.findAllByPermission(permission).stream().map(UserResponse::new).toList();
    }

    @Override
    public List<ProfileResponse> findProfilesByPermission(PermissionType permission) {
        return profileRepository.findAllByUserPermission(permission).stream().map(ProfileResponse::new).toList();
    }


}
