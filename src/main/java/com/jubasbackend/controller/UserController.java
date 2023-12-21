package com.jubasbackend.controller;

import com.jubasbackend.dto.request.UserMinimalRequest;
import com.jubasbackend.dto.request.UserRequest;
import com.jubasbackend.dto.response.UserMinimalResponse;
import com.jubasbackend.dto.response.UserResponse;
import com.jubasbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "Buscar todos os usuários.")
    @GetMapping
    public ResponseEntity<List<UserMinimalResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar usuário por id.")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Buscar todos os usuários por nível de permissão.")
    @GetMapping("/permission/{id}")
    public ResponseEntity<List<UserResponse>> findAllByUserPermission(@PathVariable Short id) {
        return ResponseEntity.ok(service.findAllByUserPermission(id));
    }

    @Operation(summary = "Cadastrar novo usuário.")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        UserResponse userCreated = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(userCreated.id()).toUri();
        return ResponseEntity.created(location).body(userCreated);
    }

    @Operation(summary = "Autenticar usuário.")
    @PostMapping("/login")
    public ResponseEntity<UserResponse> findUserAccount(@RequestBody UserMinimalRequest request) {
        return ResponseEntity.ok(service.findUserAccount(request));
    }

    @Operation(summary = "Atualizar usuário.")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(service.updateUser(id, request));
    }

}