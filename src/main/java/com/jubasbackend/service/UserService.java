package com.jubasbackend.service;

import com.jubasbackend.infrastructure.entity.UserEntity;
import com.jubasbackend.api.dto.request.UserMinimalRequest;
import com.jubasbackend.api.dto.request.UserRequest;
import com.jubasbackend.api.dto.response.UserResponse;
import com.jubasbackend.api.dto.response.UserPermissionProfileResponse;
import com.jubasbackend.api.dto.response.UserPermissionResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserEntity findUserByIdOnRepository(UUID id);

    UserEntity findUserByEmailOnRepository(String email);

    boolean existsByEmailOnRepository(String email);

    UserPermissionResponse createUser(UserRequest request);

    UserPermissionResponse findUserAccount(UserMinimalRequest request);

    UserPermissionResponse findUserById(UUID userId);

    UserPermissionProfileResponse findProfilesByUserId(UUID userId);

    List<UserResponse> findAllUsers();

    UserPermissionResponse updateUser(UUID id, UserRequest request);
}
