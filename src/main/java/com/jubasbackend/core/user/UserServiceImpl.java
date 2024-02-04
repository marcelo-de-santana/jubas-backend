package com.jubasbackend.core.user;

import com.jubasbackend.core.permission.PermissionEntity;
import com.jubasbackend.core.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<UserResponse> findUsers() {
        return repository.findAll().stream().map(UserResponse::new).toList();
    }

    @Override
    public UserPermissionResponse findUser(UUID userId) {
        return new UserPermissionResponse(findUserOnRepository(userId));
    }

    @Override
    public UserPermissionProfileResponse findProfilesByUser(UUID userId) {
        var user = findUserOnRepository(userId);
        return new UserPermissionProfileResponse(user);
    }

    @Override
    public UserPermissionResponse createUser(UserPermissionRequest request) {
        if (existsByEmailOnRepository(request.email())) {
            throw new IllegalArgumentException("User already exists.");
        }
        UserEntity userToSave = new UserEntity(request);

        return new UserPermissionResponse(repository.save(userToSave));
    }

    @Override
    public UserPermissionResponse authenticateUserAccount(UserRequest request) {
        var user = findUserOnRepository(request.email());
        if (!request.password().equals(user.getPassword())) {
            throw new IllegalArgumentException("Incorrect Email or Password.");
        }
        return new UserPermissionResponse(user);
    }

    @Override
    public UserPermissionResponse updateUser(UUID id, UserPermissionRequest request) {
        UserEntity userToUpdate = findUserOnRepository(id);

        if (!request.email().isBlank()) {
            if (!userToUpdate.getEmail().equals(request.email()) && existsByEmailOnRepository(request.email())) {
                throw new IllegalArgumentException("E-mail is already in use.");
            }
            userToUpdate.setEmail(request.email());
        }

        if (!request.password().isBlank()) {
            userToUpdate.setPassword(request.password());
        }

        if (!request.permissionId().toString().isBlank()) {
            var newPermission = PermissionEntity.builder().id(request.permissionId()).build();
            userToUpdate.setPermission(newPermission);
        }
        return new UserPermissionResponse(repository.save(userToUpdate));
    }

    private UserEntity findUserOnRepository(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User doesn't exists."));
    }

    private UserEntity findUserOnRepository(String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("The provided email is not registered in our system."));
    }

    private boolean existsByEmailOnRepository(String email) {
        return repository.existsByEmail(email);
    }
}
