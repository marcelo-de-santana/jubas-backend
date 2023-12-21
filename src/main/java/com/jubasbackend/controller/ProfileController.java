package com.jubasbackend.controller;

import com.jubasbackend.dto.request.ProfileMinimalRequest;
import com.jubasbackend.dto.request.ProfileRecoveryRequest;
import com.jubasbackend.dto.request.ProfileRequest;
import com.jubasbackend.dto.response.ProfileResponse;
import com.jubasbackend.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;

    @Operation(summary = "Buscar todos perfis associados ao usuário.")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<ProfileResponse>> findAllByUserId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findAllByUserId(id));
    }

    @Operation(summary = "Buscar todos os perfis por nível de permissão.")
    @GetMapping("/permission/{id}")
    public ResponseEntity<List<ProfileResponse>> findAllByUserPermissionId(@PathVariable Short id) {
        return ResponseEntity.ok(service.findAllProfilesByUserPermissionId(id));
    }

    @Operation(summary = "Cadastrar novo perfil de usuário.")
    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Atualizar associação do perfil de usuário.")
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateUserProfile(
            @PathVariable UUID id,
            @RequestBody ProfileRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Atualizar perfil de usuário.")
    @PatchMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable UUID id,
            @RequestBody ProfileMinimalRequest request) {
        return ResponseEntity.ok(service.updateOnlyProfile(id, request));
    }

    @Operation(summary = "Redefinir senha.")
    @PatchMapping("/recovery")
    public ResponseEntity<ProfileResponse> recovery(@RequestBody ProfileRecoveryRequest request) {
        return ResponseEntity.ok(service.recoveryPassword(request));
    }

    @Operation(summary = "Excluir perfil de usuário.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok("Profile has been deleted successfully");
    }

}
