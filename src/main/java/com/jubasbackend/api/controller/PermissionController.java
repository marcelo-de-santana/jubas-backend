package com.jubasbackend.api.controller;

import com.jubasbackend.api.dto.response.PermissionResponse;
import com.jubasbackend.api.dto.response.PermissionUserResponse;
import com.jubasbackend.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionService service;

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> findPermissions() {
        return ResponseEntity.ok(service.findPermissions());
    }

    @GetMapping("/{permissionId}/users")
    public ResponseEntity<PermissionUserResponse> findUsersByPermissionId(@PathVariable Short permissionId) {
        return ResponseEntity.ok(service.findUsersByPermissionId(permissionId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<PermissionUserResponse>> findPermissionsAndUsers() {
        return ResponseEntity.ok(service.findPermissionsAndUsers());
    }

}
