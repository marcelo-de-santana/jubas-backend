package com.jubasbackend.core.permission;

import com.jubasbackend.core.profile.dto.ProfileResponse;
import com.jubasbackend.core.user.dto.UserResponse;
import com.jubasbackend.core.user.enums.PermissionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Permissions")
@RequestMapping("/permissions")
public interface PermissionApi {
    @Operation(summary = "Buscar todos os usuários por nível de permissão.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Permissão não encontrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuários.", content = @Content)
    })
    @GetMapping("/{permission}/users")
    ResponseEntity<List<UserResponse>> findUsersByPermission(@PathVariable PermissionType permission);

    @Operation(summary = "Buscar todos os perfis por nível de permissão.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Permissão não encontrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar perfis.", content = @Content)
    })
    @GetMapping("/{permission}/profiles")
    ResponseEntity<List<ProfileResponse>> findProfilesByPermission(@PathVariable PermissionType permission);
}
