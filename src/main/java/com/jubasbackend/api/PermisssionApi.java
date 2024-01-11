package com.jubasbackend.api;

import com.jubasbackend.api.dto.response.PermissionResponse;
import com.jubasbackend.api.dto.response.PermissionUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Permission")
public interface PermisssionApi {

    @Operation(summary = "Buscar todas as permissões.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhuma permissão cadastrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar permissões.", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<PermissionResponse>> findPermissions();

    @Operation(summary = "Buscar todos os usuários associados ao nível de permissão.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Permissão não existe.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar permissões.", content = @Content)
    })
    @GetMapping("/{permissionId}/users")
    ResponseEntity<PermissionUserResponse> findUsersByPermissionId(@PathVariable Short permissionId);

    @Operation(summary = "Buscar todas as permissões e usuários associados.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhuma permissão cadastrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar permissões.", content = @Content)
    })
    @GetMapping("/users")
    ResponseEntity<List<PermissionUserResponse>> findPermissionsAndUsers();
}
