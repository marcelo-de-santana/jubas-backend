package com.jubasbackend.controller;

import com.jubasbackend.controller.request.AuthRequest;
import com.jubasbackend.controller.response.TokenResponse;
import com.jubasbackend.service.MailService;
import com.jubasbackend.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@AllArgsConstructor
@RestController
@RequestMapping
public class TokenController {

    final TokenService tokenService;
    final MailService mailService;

    @Operation(summary = "Gerar token de acesso.", responses = {
            @ApiResponse(responseCode = "200", description = "Token gerado com sucesso."),
            @ApiResponse(responseCode = "401", description = "E-mail e/ou senha incorreto(s).", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao gerar token.", content = @Content)
    })
    @PostMapping("/auth")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(tokenService.generate(request));
    }
}
