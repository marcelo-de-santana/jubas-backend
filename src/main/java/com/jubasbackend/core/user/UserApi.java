package com.jubasbackend.core.user;

import com.jubasbackend.core.user.dto.UserAuthRequest;
import com.jubasbackend.core.user.dto.UserProfileResponse;
import com.jubasbackend.core.user.dto.UserRequest;
import com.jubasbackend.core.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Users")
@RequestMapping("/users")
public interface UserApi {

    @Operation(summary = "Buscar todos os usuários.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuários.", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<UserResponse>> findUsers();

    @Operation(summary = "Buscar todos os usuários e perfis associados.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuários.", content = @Content)
    })
    @GetMapping("/profiles")
    ResponseEntity<List<UserProfileResponse>> findUsersAndProfiles();


    @Operation(summary = "Buscar usuário por id.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuário.", content = @Content)
    })
    @GetMapping("/{userId}")
    ResponseEntity<UserResponse> findUser(@PathVariable UUID userId);

    @Operation(summary = "Buscar perfis associados ao usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuário.", content = @Content)
    })
    @GetMapping("/{userId}/profiles")
    ResponseEntity<UserProfileResponse> findProfilesByUser(@PathVariable UUID userId);

    @Operation(summary = "Cadastrar novo usuário.", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "401", description = "E-mail já cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar usuário.", content = @Content)
    })
    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request);

    @Operation(summary = "Autenticar usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Senha incorreta.", content = @Content),
            @ApiResponse(responseCode = "404", description = "E-mail não cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao autenticar usuário.", content = @Content)
    })
    @PostMapping("/auth")
    ResponseEntity<UserResponse> authenticateUserAccount(@RequestBody UserAuthRequest request);

    @Operation(summary = "Atualizar usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Erro ao encontrar id de usuário ou e-mail.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar usuário.", content = @Content)
    })
    @PatchMapping("/{userId}")
    ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId, @RequestBody UserRequest request);
}
