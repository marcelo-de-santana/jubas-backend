package com.jubasbackend.core.user;

import com.jubasbackend.core.permission.PermissionEntity;
import com.jubasbackend.core.user.dto.UserRequest;
import com.jubasbackend.core.user.dto.UserPermissionRequest;
import com.jubasbackend.core.user.dto.UserPermissionProfileResponse;
import com.jubasbackend.core.user.dto.UserPermissionResponse;
import com.jubasbackend.core.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserEntity findUserByIdOnRepository(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User doesn't exists."));
    }

    public UserEntity findUserByEmailOnRepository(String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("The provided email is not registered in our system."));
    }

    public boolean existsByEmailOnRepository(String email) {
        return repository.existsByEmail(email);
    }

    public UserPermissionResponse createUser(UserPermissionRequest request) {
        if (existsByEmailOnRepository(request.email())) {
            throw new IllegalArgumentException("User already exists.");
        }
        UserEntity userToSave = new UserEntity(request);

        return new UserPermissionResponse(repository.save(userToSave));
    }

    public UserPermissionResponse findUserAccount(UserRequest request) {
        var user = findUserByEmailOnRepository(request.email());
        if (!request.password().equals(user.getPassword())) {
            throw new IllegalArgumentException("Incorrect Email or Password.");
        }
        return new UserPermissionResponse(user);
    }

    public UserPermissionResponse findUserById(UUID userId) {
        return new UserPermissionResponse(findUserByIdOnRepository(userId));
    }

    public UserPermissionProfileResponse findProfilesByUserId(UUID userId) {
        var user = findUserByIdOnRepository(userId);
        return new UserPermissionProfileResponse(user);
    }

    public List<UserResponse> findAllUsers() {
        return repository.findAll().stream().map(UserResponse::new).toList();
    }

    public UserPermissionResponse updateUser(UUID id, UserPermissionRequest request) {
        UserEntity userToUpdate = findUserByIdOnRepository(id);

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

}
