package com.jubasbackend.controller;

import com.jubasbackend.dto.request.RequestMinimalProfileDTO;
import com.jubasbackend.dto.request.RequestProfileDTO;
import com.jubasbackend.dto.response.ResponseProfileDTO;
import com.jubasbackend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ResponseProfileDTO>> findAllByUserId(@PathVariable UUID id) {
        return ResponseEntity.ok(profileService.findAllByUserId(id));
    }

    @GetMapping("/permission/{id}")
    public ResponseEntity<List<ResponseProfileDTO>> findAllByUserPermissionId(@PathVariable Short id) {
        return ResponseEntity.ok(profileService.findAllProfilesByUserPermissionId(id));
    }

    @PostMapping
    public ResponseEntity<ResponseProfileDTO> createProfile(@RequestBody RequestProfileDTO profile) {
        return ResponseEntity.ok(profileService.create(profile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseProfileDTO> updateProfile(@PathVariable UUID id, @RequestBody RequestProfileDTO profile) {
        return ResponseEntity.ok(profileService.update(id, profile));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseProfileDTO> updateProfile(@PathVariable UUID id, @RequestBody RequestMinimalProfileDTO profile) {
        return ResponseEntity.ok(profileService.updateOnlyProfile(id, profile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseProfileDTO> deleteProfile(@PathVariable UUID id){
        return ResponseEntity.ok(profileService.delete(id));
    }

}
