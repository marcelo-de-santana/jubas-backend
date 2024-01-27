package com.jubasbackend.core.user;

import com.jubasbackend.core.user.dto.UserRequest;
import com.jubasbackend.core.user.dto.UserPermissionRequest;
import com.jubasbackend.core.user.dto.UserPermissionResponse;
import com.jubasbackend.core.user.dto.UserPermissionProfileResponse;
import com.jubasbackend.core.user.dto.UserResponse;
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

    public ResponseEntity<List<UserResponse>> findAllUsers() {
        return ResponseEntity.ok(service.findAllUsers());
    }

    public ResponseEntity<UserPermissionResponse> findUserById(UUID userId) {
        return ResponseEntity.ok(service.findUserById(userId));
    }

    public ResponseEntity<UserPermissionProfileResponse> findProfilesByUserId(UUID userId) {
        return ResponseEntity.ok(service.findProfilesByUserId(userId));
    }

    public ResponseEntity<UserPermissionResponse> createUser(UserPermissionRequest request) {
        UserPermissionResponse userCreated = service.createUser(request);
        return ResponseEntity.created(URI.create("/user/" + userCreated.id())).body(userCreated);
    }

    public ResponseEntity<UserPermissionResponse> findUserAccount(UserRequest request) {
        return ResponseEntity.ok(service.findUserAccount(request));
    }

    public ResponseEntity<UserPermissionResponse> updateUser(UUID userId, UserPermissionRequest request) {
        return ResponseEntity.ok(service.updateUser(userId, request));
    }

}
