package com.jubasbackend.controller;

import com.jubasbackend.dto.profile.ProfileDTO;
import com.jubasbackend.domain.entity.Profile;
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

    @GetMapping("/{id}")
    public ResponseEntity<List<ProfileDTO>> findAllByUserId(@PathVariable UUID id) {
        return ResponseEntity.ok(profileService.findAllByUserId(id));
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody Profile profile){
        return ResponseEntity.ok(profileService.updateProfile(profile));
    }
}
