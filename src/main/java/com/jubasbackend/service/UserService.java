package com.jubasbackend.service;

import com.jubasbackend.controller.request.UserRequest;
import com.jubasbackend.controller.response.UserResponse;
import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.enums.PermissionType;
import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.exception.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.jubasbackend.utils.TokenUtils.hasAuthority;
import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository repository;
    final PasswordEncoder passwordEncoder;

    public List<UserResponse> getUsers() {
        return repository.findAll().stream()
                .map(UserResponse::new)
                .toList();
    }

    public List<UserResponse.WithProfiles> getUsersWithProfiles() {
        return repository.findAll().stream()
                .map(UserResponse.WithProfiles::new)
                .toList();
    }

    public UserResponse.WithProfiles getUser(UUID userId) {
        return new UserResponse.WithProfiles(findUser(userId));
    }

    public UserResponse createUser(UserRequest request, JwtAuthenticationToken jwt) {
        if (jwt != null && isAdmin(jwt))
            return create(request, request.permission());

        return create(request, PermissionType.CLIENTE);
    }

    public UserResponse updateUser(UUID id, UserRequest request) {
        User userToUpdate = findUser(id);

        if (request.email() != null) {
            if (!userToUpdate.getEmail().equals(request.email()) && existsByEmail(request.email())) {
                throw new IllegalArgumentException("E-mail is already in use.");
            }
            userToUpdate.setEmail(request.email());
        }

        if (request.password() != null)
            userToUpdate.setPassword(request.password());

        if (request.permission() != null)
            userToUpdate.setPermission(request.permission());

        return new UserResponse(repository.save(userToUpdate));
    }

    UserResponse create(UserRequest request, PermissionType permission) {
        if (existsByEmail(request.email()))
            throw new APIException(CONFLICT, "User already exists.");

        var newUser = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .permission(permission)
                .build();

        return new UserResponse(repository.save(newUser));
    }

    User findUser(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User doesn't exists."));
    }

    boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    boolean isAdmin(JwtAuthenticationToken jwt) {
        return hasAuthority(jwt, "SCOPE_ADMIN");
    }
}
