package com.jubasbackend.api;

import com.jubasbackend.api.dto.request.ProfileRecoveryRequest;
import com.jubasbackend.api.dto.request.ProfileRequest;
import com.jubasbackend.api.dto.request.ProfileUserRequest;
import com.jubasbackend.api.dto.response.ProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Profile")
@RequestMapping("/profile")
public interface ProfileApi {

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
