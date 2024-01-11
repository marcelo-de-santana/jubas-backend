package com.jubasbackend.api.controller;

import com.jubasbackend.api.PermisssionApi;
import com.jubasbackend.api.dto.response.PermissionResponse;
import com.jubasbackend.api.dto.response.PermissionUserResponse;
import com.jubasbackend.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/permission")
public class PermissionController implements PermisssionApi {

    private final PermissionService service;

    public ResponseEntity<List<PermissionResponse>> findPermissions() {
        return ResponseEntity.ok(service.findPermissions());
    }

    public ResponseEntity<PermissionUserResponse> findUsersByPermissionId(@PathVariable Short permissionId) {
        return ResponseEntity.ok(service.findUsersByPermissionId(permissionId));
    }

    public ResponseEntity<List<PermissionUserResponse>> findPermissionsAndUsers() {
        return ResponseEntity.ok(service.findPermissionsAndUsers());
    }

}
