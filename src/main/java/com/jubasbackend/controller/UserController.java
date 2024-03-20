package com.jubasbackend.controller;

import com.jubasbackend.controller.request.UserAuthRequest;
import com.jubasbackend.controller.request.UserRequest;
import com.jubasbackend.controller.response.UserProfileResponse;
import com.jubasbackend.controller.response.UserResponse;
import com.jubasbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Operation(summary = "Buscar todos os usuários.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuários.", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> findUsers() {
        return ResponseEntity.ok(service.findUsers());
    }

    @Operation(summary = "Buscar todos os usuários e perfis associados.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuários.", content = @Content)
    })
    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfileResponse>> findUsersAndProfiles() {
        return ResponseEntity.ok(service.findUsersAndProfiles());
    }


    @Operation(summary = "Buscar usuário por id.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuário.", content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> findUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.findUser(userId));
    }

    @Operation(summary = "Buscar perfis associados ao usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuário.", content = @Content)
    })
    @GetMapping("/{userId}/profiles")
    public ResponseEntity<UserProfileResponse> findProfilesByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.findProfilesByUser(userId));

    }

    @Operation(summary = "Cadastrar novo usuário.", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "401", description = "E-mail já cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar usuário.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        var userCreated = service.createUser(request);
        return ResponseEntity.created(URI.create("/users/" + userCreated.id())).body(userCreated);
    }

    @Operation(summary = "Autenticar usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Senha incorreta.", content = @Content),
            @ApiResponse(responseCode = "404", description = "E-mail não cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao autenticar usuário.", content = @Content)
    })
    @PostMapping("/auth")
    public ResponseEntity<UserResponse> authenticateUserAccount(@RequestBody UserAuthRequest request) {
        return ResponseEntity.ok(service.authenticateUserAccount(request));
    }

    @Operation(summary = "Atualizar usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Erro ao encontrar id de usuário ou e-mail.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar usuário.", content = @Content)
    })
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId, @RequestBody UserRequest request) {
        return ResponseEntity.ok(service.updateUser(userId, request));
    }
}
