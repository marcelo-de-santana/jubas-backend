package com.jubasbackend.api.controller;

import com.jubasbackend.api.UserApi;
import com.jubasbackend.api.dto.request.UserMinimalRequest;
import com.jubasbackend.api.dto.request.UserRequest;
import com.jubasbackend.api.dto.response.UserPermissionResponse;
import com.jubasbackend.api.dto.response.UserProfileResponse;
import com.jubasbackend.api.dto.response.UserResponse;
import com.jubasbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService service;

    public ResponseEntity<List<UserResponse>> findAllUsers() {
        return ResponseEntity.ok(service.findAllUsers());
    }


    public ResponseEntity<UserPermissionResponse> findUserById(UUID userId) {
        return ResponseEntity.ok(service.findUserById(userId));
    }

    public ResponseEntity<UserProfileResponse> findProfilesByUserId(UUID userId) {
        return ResponseEntity.ok(service.findProfilesByUserId(userId));
    }

    public ResponseEntity<UserPermissionResponse> createUser(UserRequest request) {
        UserPermissionResponse userCreated = service.createUser(request);
        return ResponseEntity.created(URI.create("/user" + userCreated.id())).body(userCreated);
    }

    public ResponseEntity<UserPermissionResponse> findUserAccount(UserMinimalRequest request) {
        return ResponseEntity.ok(service.findUserAccount(request));
    }

    public ResponseEntity<UserPermissionResponse> updateUser(UUID userId, UserRequest request) {
        return ResponseEntity.ok(service.updateUser(userId, request));
    }

}
