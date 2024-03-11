package com.jubasbackend.core.user;

import com.jubasbackend.core.user.dto.UserAuthRequest;
import com.jubasbackend.core.user.dto.UserProfileResponse;
import com.jubasbackend.core.user.dto.UserRequest;
import com.jubasbackend.core.user.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserResponse> findUsers();

    List<UserProfileResponse> findUsersAndProfiles();

    UserResponse findUser(UUID userId);

    UserProfileResponse findProfilesByUser(UUID userId);

    UserResponse createUser(UserRequest request);

    UserResponse authenticateUserAccount(UserAuthRequest request);

    UserResponse updateUser(UUID id, UserRequest request);
}
