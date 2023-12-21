package com.jubasbackend.service;

import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.UserPermission;
import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.dto.request.UserMinimalRequest;
import com.jubasbackend.dto.request.UserRequest;
import com.jubasbackend.dto.response.UserMinimalResponse;
import com.jubasbackend.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    protected User findUserById(UUID id) {
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

    public UserResponse create(UserRequest request) {
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

    public UserResponse findById(UUID id) {
        return new UserResponse(findUserById(id));
    }

    public List<UserMinimalResponse> findAll() {
        return repository.findAll().stream().map(UserMinimalResponse::new).toList();
    }

    public List<UserResponse> findAllByUserPermission(Short id) {
        return (repository.findUsersByUserPermission_Id(id).stream().map(UserResponse::new).toList());
    }

    public UserResponse updateUser(UUID id, UserRequest request) {
        User userToUpdate = findUserById(id);
        if (request.password() != null && request.password().length() >= 8) {
            userToUpdate.setPassword(request.password());
        }
        userToUpdate.setEmail(request.email());
        userToUpdate.setUserPermission(new UserPermission(request.userPermissionId()));
        return new UserResponse(repository.save(userToUpdate));
    }

}