package com.jubasbackend.core.user;

import com.jubasbackend.core.user.dto.UserRequest;
import com.jubasbackend.core.user.dto.UserPermissionRequest;
import com.jubasbackend.core.user.dto.UserResponse;
import com.jubasbackend.core.user.dto.UserPermissionProfileResponse;
import com.jubasbackend.core.user.dto.UserPermissionResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserEntity findUserByIdOnRepository(UUID id);

    UserEntity findUserByEmailOnRepository(String email);

    boolean existsByEmailOnRepository(String email);

    UserPermissionResponse createUser(UserPermissionRequest request);

    UserPermissionResponse findUserAccount(UserRequest request);

    UserPermissionResponse findUserById(UUID userId);

    UserPermissionProfileResponse findProfilesByUserId(UUID userId);

    List<UserResponse> findAllUsers();

    UserPermissionResponse updateUser(UUID id, UserPermissionRequest request);
}
