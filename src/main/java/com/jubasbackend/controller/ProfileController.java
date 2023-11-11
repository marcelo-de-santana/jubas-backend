package com.jubasbackend.controller;

import com.jubasbackend.dto.request.ProfileMinimalRequest;
import com.jubasbackend.dto.request.ProfileRecoveryRequest;
import com.jubasbackend.dto.request.ProfileRequest;
import com.jubasbackend.dto.response.ProfileResponse;
import com.jubasbackend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService service;

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ProfileResponse>> findAllByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.findAllByUserId(userId));
    }

    @GetMapping("/permission/{id}")
    public ResponseEntity<List<ProfileResponse>> findAllByUserPermissionId(@PathVariable Short permissionId) {
        return ResponseEntity.ok(service.findAllProfilesByUserPermissionId(permissionId));
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable UUID id,
            @RequestBody ProfileRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable UUID id,
            @RequestBody ProfileMinimalRequest request) {
        return ResponseEntity.ok(service.updateOnlyProfile(id, request));
    }

    @PatchMapping("/recovery")
    public ResponseEntity<ProfileResponse> recovery(@RequestBody ProfileRecoveryRequest request) {
        return ResponseEntity.ok(service.recoveryPassword(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Profile has been deleted successfully");
    }

}
