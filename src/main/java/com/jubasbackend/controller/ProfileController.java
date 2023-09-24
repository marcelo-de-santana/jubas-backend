package com.jubasbackend.controller;

import com.jubasbackend.domain.entity.Profile;
import com.jubasbackend.dto.profile.ProfileDTO;
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
    public ResponseEntity<List<ProfileDTO>> findAllByUserId(@PathVariable UUID id) {
        return ResponseEntity.ok(profileService.findAllByUserId(id));
    }

    @PutMapping("/user")
    public ResponseEntity<ProfileDTO> updateUserAndProfile(@RequestBody Profile profile){
        return ResponseEntity.ok(profileService.updateUserAndProfile(profile));
    }

    @PostMapping
    public ResponseEntity<ProfileDTO> createProfile(@RequestBody Profile profile){
        return ResponseEntity.ok(profileService.create(profile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProfileDTO> deleteProfile(@PathVariable UUID id){
        return ResponseEntity.ok(profileService.delete(id));
    }

    @GetMapping("/user/permission/{id}")
    public ResponseEntity<List<ProfileDTO>> findAllProfilesByUserPermissionId(@PathVariable Short id){
        return ResponseEntity.ok(profileService.findAllProfilesByUserPermissionId(id));
    }

    @PutMapping
    public ResponseEntity<ProfileDTO> updateProfile(@RequestBody Profile profile){
        return ResponseEntity.ok(profileService.updateProfile(profile));
    }
}
