package com.jubasbackend.controller;

import com.jubasbackend.controller.request.MailRequest;
import com.jubasbackend.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mail")
@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class MailController {

    private final MailService mailService;

    @Operation(summary = "Enviar notificação.", responses = {
            @ApiResponse(responseCode = "200", description = "Notificação realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao enviar notificação.")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public void sendMail(@RequestBody @Valid MailRequest request) {
        mailService.sendEmail(request);
    }
}
