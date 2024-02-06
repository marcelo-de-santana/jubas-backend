package com.jubasbackend.core.profile;

import com.jubasbackend.core.profile.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Profiles")
@RequestMapping("/profiles")
public interface ProfileApi {

    @Operation(summary = "Buscar todos os perfis.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum perfil encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar perfis.", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<ProfileResponse>> findProfiles();

    @Operation(summary = "Buscar perfis e usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso na recuperação dos perfis com usuários e permissões."),
            @ApiResponse(responseCode = "404", description = "Nenhum perfil encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar os perfis.", content = @Content)
    })
    @GetMapping("/user/permission")
    ResponseEntity<List<ProfileUserPermissionResponse>> findProfilesAndUser();

    @Operation(summary = "Cadastrar novo perfil de usuário.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não existe.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar perfil.", content = @Content)
    })
    @PostMapping
    ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileUserRequest request);

    @Operation(summary = "Redefinir senha.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum perfil encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao alterar senha.", content = @Content)
    })
    @PostMapping("/recovery-password")
    ResponseEntity<Void> recoveryPassword(@RequestBody ProfileRecoveryRequest request);

    @Operation(summary = "Atualizar perfil de usuário.", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Perfil não existe.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao atual perfil.", content = @Content)
    })
    @PatchMapping("/{profileId}")
    ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable UUID profileId,
            @RequestBody ProfileRequest request);


    @Operation(summary = "Excluir perfil de usuário.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Perfil atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Perfil não existe.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao atual perfil.", content = @Content)
    })
    @DeleteMapping("/{profileId}")
    ResponseEntity<Void> deleteProfile(@PathVariable UUID profileId);

}
