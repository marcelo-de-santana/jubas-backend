package com.jubasbackend.controller;

import com.jubasbackend.dto.request.UserMinimalRequest;
import com.jubasbackend.dto.request.UserRequest;
import com.jubasbackend.dto.response.UserMinimalResponse;
import com.jubasbackend.dto.response.UserResponse;
import com.jubasbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "Buscar todos os usuários.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuários.")
    })
    @GetMapping
    public ResponseEntity<List<UserMinimalResponse>> findAllUsers() {
        return ResponseEntity.ok(service.findAllUsers());
    }

    @Operation(summary = "Buscar usuário por id.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuário.")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.findUserById(userId));
    }

    @Operation(summary = "Buscar perfis associados ao usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuário.")
    })
    @GetMapping("/{userId}/profile")
    public ResponseEntity<?> findProfilesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.findProfilesByUserId(userId));
    }

    @Operation(summary = "Buscar todos os usuários por nível de permissão.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuários.")
    })
    @GetMapping("/permission/{permissionId}")
    public ResponseEntity<List<UserResponse>> findAllUsersByPermissionId(@PathVariable Short permissionId) {
        return ResponseEntity.ok(service.findAllUsersByPermissionId(permissionId));
    }

    @Operation(summary = "Cadastrar novo usuário.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "401", description = "E-mail já cadastrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar usuário.")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        UserResponse userCreated = service.createUser(request);
        return ResponseEntity.created(URI.create("/user" + userCreated.id())).body(userCreated);
    }

    @Operation(summary = "Autenticar usuário.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Senha incorreta."),
            @ApiResponse(responseCode = "404", description = "E-mail não cadastrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao autenticar usuário.")
    })
    @PostMapping("/login")
    public ResponseEntity<UserResponse> findUserAccount(@RequestBody UserMinimalRequest request) {
        return ResponseEntity.ok(service.findUserAccount(request));
    }

    @Operation(summary = "Atualizar usuário.", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Erro ao encontrar id de usuário ou e-mail."),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar usuário.")
    })
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId, @RequestBody UserRequest request) {
        return ResponseEntity.ok(service.updateUser(userId, request));
    }

}
