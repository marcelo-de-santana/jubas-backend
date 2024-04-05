package com.jubasbackend.controller;

import com.jubasbackend.controller.request.ProfileRequest;
import com.jubasbackend.controller.request.RecoveryPasswordRequest;
import com.jubasbackend.controller.response.ProfileResponse;
import com.jubasbackend.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
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
    public ResponseEntity<List<? extends ProfileResponse>> findProfiles(
            @RequestParam(required = false, defaultValue = "false") boolean user) {
        return ResponseEntity.ok(user ? service.findProfilesWithUser() : service.findProfiles());
    }

    @Operation(summary = "Cadastrar novo perfil de usuário.", responses = {
            @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não existe."),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar perfil.")
    })
    @PostMapping
    public ResponseEntity<Void> createProfile(@NonNull @RequestBody ProfileRequest request) {
        var profileCreated = service.createProfile(request);
        return ResponseEntity.created(URI.create("/profiles/" + profileCreated.getId())).build();
    }

    @Operation(summary = "Redefinir senha.", responses = {
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum perfil encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao alterar senha.")
    })
    @PostMapping("/recovery-password")
    public ResponseEntity<Void> recoveryPassword(@RequestBody RecoveryPasswordRequest request) {
        service.recoveryPassword(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar perfil de usuário.", responses = {
            @ApiResponse(responseCode = "204", description = "Perfil atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Perfil não existe."),
            @ApiResponse(responseCode = "500", description = "Erro ao atual perfil.")
    })
    @PatchMapping("/{profileId}")
    public ResponseEntity<Void> updateProfile(@PathVariable UUID profileId, @RequestBody ProfileRequest request) {
        service.updateProfile(profileId, request);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Excluir perfil de usuário.", responses = {
            @ApiResponse(responseCode = "204", description = "Perfil atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Perfil não existe."),
            @ApiResponse(responseCode = "500", description = "Erro ao atual perfil.")
    })
    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID profileId) {
        service.deleteProfile(profileId);
        return ResponseEntity.noContent().build();
    }

}
