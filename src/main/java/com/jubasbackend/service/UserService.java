package com.jubasbackend.service;

import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.UserPermission;
import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.dto.request.UserMinimalRequest;
import com.jubasbackend.dto.request.UserRequest;
import com.jubasbackend.dto.response.UserMinimalResponse;
import com.jubasbackend.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    protected User findUserById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User doesn't exists."));
    }

    protected boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public UserResponse create(UserRequest request) {
        if (existsByEmail(request.email())) {
            throw new IllegalArgumentException("User already exists.");
        }
        existsByEmail(request.email());
        User userToSave = new User(request);
        return new UserResponse(repository.save(userToSave));
    }

    public UserResponse findUserAccount(UserMinimalRequest request) {
        if (!existsByEmail(request.email())) {
            throw new NoSuchElementException("User doesn't exists.");
        }
        return new UserResponse(repository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(
                        () -> new IllegalArgumentException("Incorrect Email or Password.")));
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