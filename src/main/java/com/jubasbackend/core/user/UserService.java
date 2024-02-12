package com.jubasbackend.core.user;

import com.jubasbackend.core.user.dto.*;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserResponse> findUsers();

    UserResponse findUser(UUID userId);

    UserProfileResponse findProfilesByUser(UUID userId);

    UserResponse createUser(UserRequest request);

    UserResponse authenticateUserAccount(UserAuthRequest request);

    UserResponse updateUser(UUID id, UserRequest request);
}
