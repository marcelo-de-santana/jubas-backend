package com.jubasbackend.core.user;

import com.jubasbackend.core.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService service;

    @Override
    public ResponseEntity<List<UserResponse>> findUsers() {
        return ResponseEntity.ok(service.findUsers());
    }

    @Override
    public ResponseEntity<UserResponse> findUser(UUID userId) {
        return ResponseEntity.ok(service.findUser(userId));
    }

    @Override
    public ResponseEntity<UserProfileResponse> findProfilesByUser(UUID userId) {
        return ResponseEntity.ok(service.findProfilesByUser(userId));
    }

    @Override
    public ResponseEntity<UserResponse> createUser(UserRequest request) {
        UserResponse userCreated = service.createUser(request);
        return ResponseEntity.created(URI.create("/users/" + userCreated.id())).body(userCreated);
    }

    @Override
    public ResponseEntity<UserResponse> authenticateUserAccount(UserAuthRequest request) {
        return ResponseEntity.ok(service.authenticateUserAccount(request));
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID userId, UserRequest request) {
        return ResponseEntity.ok(service.updateUser(userId, request));
    }

}
