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
    public ResponseEntity<UserPermissionResponse> findUser(UUID userId) {
        return ResponseEntity.ok(service.findUser(userId));
    }

    @Override
    public ResponseEntity<UserPermissionProfileResponse> findProfilesByUser(UUID userId) {
        return ResponseEntity.ok(service.findProfilesByUser(userId));
    }

    @Override
    public ResponseEntity<UserPermissionResponse> createUser(UserPermissionRequest request) {
        UserPermissionResponse userCreated = service.createUser(request);
        return ResponseEntity.created(URI.create("/users/" + userCreated.id())).body(userCreated);
    }

    @Override
    public ResponseEntity<UserPermissionResponse> authenticateUserAccount(UserRequest request) {
        return ResponseEntity.ok(service.authenticateUserAccount(request));
    }

    @Override
    public ResponseEntity<UserPermissionResponse> updateUser(UUID userId, UserPermissionRequest request) {
        return ResponseEntity.ok(service.updateUser(userId, request));
    }

}
