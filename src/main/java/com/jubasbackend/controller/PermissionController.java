package com.jubasbackend.controller;

import com.jubasbackend.controller.response.ProfileResponse;
import com.jubasbackend.controller.response.UserResponse;
import com.jubasbackend.domain.entity.enums.PermissionType;
import com.jubasbackend.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Permissions")
@RequiredArgsConstructor
@RestController
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService service;

    @Operation(summary = "Buscar todos os usuários por nível de permissão.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Permissão não encontrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar usuários.", content = @Content)
    })
    @GetMapping("/{permission}/users")
    ResponseEntity<List<? extends UserResponse>> findUsersByPermission(@PathVariable PermissionType permission) {
        return ResponseEntity.ok(service.findUsersByPermission(permission));
    }

    @Operation(summary = "Buscar todos os perfis por nível de permissão.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Permissão não encontrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar perfis.", content = @Content)
    })
    @GetMapping("/{permission}/profiles")
    ResponseEntity<List<ProfileResponse>> findProfilesByPermission(@PathVariable PermissionType permission) {
        return ResponseEntity.ok(service.findProfilesByPermission(permission));
    }
}
