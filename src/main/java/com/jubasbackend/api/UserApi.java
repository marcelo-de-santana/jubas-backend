package com.jubasbackend.api;

import com.jubasbackend.api.dto.request.UserMinimalRequest;
import com.jubasbackend.api.dto.request.UserRequest;
import com.jubasbackend.api.dto.response.UserPermissionResponse;
import com.jubasbackend.api.dto.response.UserProfileResponse;
import com.jubasbackend.api.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface UserApi {

    @Operation(summary = "Buscar todos os usuários.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuários.", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<UserResponse>> findAllUsers();

    @Operation(summary = "Buscar usuário por id.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuário.", content = @Content)
    })
    @GetMapping("/{userId}")
    ResponseEntity<UserPermissionResponse> findUserById(@PathVariable UUID userId);

    @Operation(summary = "Buscar perfis associados ao usuário.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuário.", content = @Content)
    })
    @GetMapping("/{userId}/profile")
    ResponseEntity<UserProfileResponse> findProfilesByUserId(@PathVariable UUID userId);

    @Operation(summary = "Cadastrar novo usuário.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "401", description = "E-mail já cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar usuário.", content = @Content)
    })
    @PostMapping
    ResponseEntity<UserPermissionResponse> createUser(@RequestBody UserRequest request);

    @Operation(summary = "Autenticar usuário.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Senha incorreta.", content = @Content),
            @ApiResponse(responseCode = "404", description = "E-mail não cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao autenticar usuário.", content = @Content)
    })
    @PostMapping("/login")
    ResponseEntity<UserPermissionResponse> findUserAccount(@RequestBody UserMinimalRequest request);

    @Operation(summary = "Atualizar usuário.", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Erro ao encontrar id de usuário ou e-mail.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar usuário.", content = @Content)
    })
    @PatchMapping("/{userId}")
    ResponseEntity<UserPermissionResponse> updateUser(@PathVariable UUID userId, @RequestBody UserRequest request);
}
