package com.jubasbackend.service;

import com.jubasbackend.domain.entity.Permission;
import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.dto.request.UserMinimalRequest;
import com.jubasbackend.dto.request.UserRequest;
import com.jubasbackend.dto.response.UserMinimalResponse;
import com.jubasbackend.dto.response.UserProfileResponse;
import com.jubasbackend.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.jubasbackend.utils.StringUtils.isNonBlank;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    protected User findUserByIdOnRepository(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User doesn't exists."));
    }

    protected User findUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> new NoSuchElementException("The provided email is not registered in our system."));
    }

    protected boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public UserResponse createUser(UserRequest request) {
        if (existsByEmail(request.email())) {
            throw new IllegalArgumentException("User already exists.");
        }
        User userToSave = new User(request);

        return new UserResponse(repository.save(userToSave));
    }

    public UserResponse findUserAccount(UserMinimalRequest request) {
        var user = findUserByEmail(request.email());
        if (!request.password().equals(user.getPassword())) {
            throw new IllegalArgumentException("Incorrect Email or Password.");
        }
        return new UserResponse(user);
    }

    public UserResponse findUserById(UUID userId) {
        return new UserResponse(findUserByIdOnRepository(userId));
    }

    public UserProfileResponse findProfilesByUserId(UUID userId) {
        var user = findUserByIdOnRepository(userId);
        return new UserProfileResponse(user);
    }

    public List<UserMinimalResponse> findAllUsers() {
        return repository.findAll().stream().map(UserMinimalResponse::new).toList();
    }

    public List<UserResponse> findAllUsersByPermissionId(Short permissionId) {
        return (repository.findUsersByPermissionId(permissionId).stream().map(UserResponse::new).toList());
    }

    public UserResponse updateUser(UUID id, UserRequest request) {
        User userToUpdate = findUserByIdOnRepository(id);

        if (isNonBlank(request.email())) {
            if (!userToUpdate.getEmail().equals(request.email()) && existsByEmail(request.email())) {
                throw new IllegalArgumentException("E-mail is already in use.");
            }
            userToUpdate.setEmail(request.email());
        }

        if (isNonBlank(request.password())) {
            userToUpdate.setPassword(request.password());
        }

        if (isNonBlank(request.permissionId())) {
            var newPermission = Permission.builder().id(request.permissionId()).build();
            userToUpdate.setPermission(newPermission);
        }
        return new UserResponse(repository.save(userToUpdate));
    }

}
