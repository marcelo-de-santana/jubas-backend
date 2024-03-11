package com.jubasbackend.core.user;

import com.jubasbackend.core.user.dto.UserAuthRequest;
import com.jubasbackend.core.user.dto.UserProfileResponse;
import com.jubasbackend.core.user.dto.UserRequest;
import com.jubasbackend.core.user.dto.UserResponse;
import com.jubasbackend.core.user.enums.PermissionType;
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
    public List<UserProfileResponse> findUsersAndProfiles() {
        return repository.findAll().stream().map(UserProfileResponse::new).toList();
    }

    @Override
    public UserResponse findUser(UUID userId) {
        return new UserResponse(findUserOnRepository(userId));
    }

    @Override
    public UserProfileResponse findProfilesByUser(UUID userId) {
        var user = findUserOnRepository(userId);
        return new UserProfileResponse(user);
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        if (existsByEmailOnRepository(request.email())) {
            throw new IllegalArgumentException("User already exists.");
        }
        UserEntity userToSave = new UserEntity(request);

        return new UserResponse(repository.save(userToSave));
    }

    @Override
    public UserResponse authenticateUserAccount(UserAuthRequest request) {
        var user = findUserOnRepository(request.email());
        if (!request.password().equals(user.getPassword())) {
            throw new IllegalArgumentException("Incorrect Email or Password.");
        }
        return new UserResponse(user);
    }

    @Override
    public UserResponse updateUser(UUID id, UserRequest request) {
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

        if (!request.permission().toString().isBlank()) {
            userToUpdate.setPermission(request.permission());
        }
        return new UserResponse(repository.save(userToUpdate));
    }

    private UserEntity findUserOnRepository(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User doesn't exists."));
    }

    private UserEntity findUserOnRepository(String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("The provided email is not registered in our system."));
    }

    private List<UserEntity> findUsersByPermission(PermissionType permission){
        return repository.findAllByPermission(permission);
    }

    private boolean existsByEmailOnRepository(String email) {
        return repository.existsByEmail(email);
    }
}
