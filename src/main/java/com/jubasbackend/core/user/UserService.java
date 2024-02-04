package com.jubasbackend.core.user;

import com.jubasbackend.core.user.dto.*;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserResponse> findUsers();

    UserPermissionResponse findUser(UUID userId);

    UserPermissionProfileResponse findProfilesByUser(UUID userId);

    UserPermissionResponse createUser(UserPermissionRequest request);

    UserPermissionResponse authenticateUserAccount(UserRequest request);

    UserPermissionResponse updateUser(UUID id, UserPermissionRequest request);
}
