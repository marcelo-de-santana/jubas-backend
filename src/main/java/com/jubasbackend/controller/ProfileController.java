package com.jubasbackend.controller;

import com.jubasbackend.controller.response.ProfileUserPermissionResponse;
import com.jubasbackend.controller.request.ProfileRecoveryRequest;
import com.jubasbackend.controller.request.ProfileRequest;
import com.jubasbackend.controller.request.ProfileUserRequest;
import com.jubasbackend.controller.response.ProfileResponse;
import com.jubasbackend.service.ProfileService;
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

@Tag(name = "Profiles")
@RequiredArgsConstructor
@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService service;

    @Operation(summary = "Buscar todos os perfis.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum perfil encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar perfis.", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ProfileResponse>> findProfiles() {
        return ResponseEntity.ok(service.findProfiles());
    }

    @Operation(summary = "Buscar perfis e usuário", responses = {
            @ApiResponse(responseCode = "200", description = "Sucesso na recuperação dos perfis com usuários e permissões."),
            @ApiResponse(responseCode = "404", description = "Nenhum perfil encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar os perfis.", content = @Content)
    })
    @GetMapping("/user/permission")
    public ResponseEntity<List<ProfileUserPermissionResponse>> findProfilesAndUser() {
        return ResponseEntity.ok(service.findProfilesAndUser());
    }

    @Operation(summary = "Cadastrar novo perfil de usuário.", responses = {
            @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não existe.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar perfil.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileUserRequest request) {
        var profileCreated = service.createProfile(request);
        return ResponseEntity.created(URI.create("/profiles/" + profileCreated.id())).body(profileCreated);
    }

    @Operation(summary = "Redefinir senha.", responses = {
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum perfil encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao alterar senha.", content = @Content)
    })
    @PostMapping("/recovery-password")
    public ResponseEntity<Void> recoveryPassword(@RequestBody ProfileRecoveryRequest request) {
        service.recoveryPassword(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar perfil de usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Perfil não existe.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao atual perfil.", content = @Content)
    })
    @PatchMapping("/{profileId}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable UUID profileId,
            @RequestBody ProfileRequest request) {
        return ResponseEntity.ok(service.updateProfile(profileId, request));
    }


    @Operation(summary = "Excluir perfil de usuário.", responses = {
            @ApiResponse(responseCode = "204", description = "Perfil atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Perfil não existe.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao atual perfil.", content = @Content)
    })
    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID profileId) {
        service.deleteProfile(profileId);
        return ResponseEntity.noContent().build();
    }

}
