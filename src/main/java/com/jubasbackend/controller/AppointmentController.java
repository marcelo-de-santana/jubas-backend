package com.jubasbackend.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.controller.request.AppointmentRequest;
import com.jubasbackend.controller.response.AppointmentResponse;
import com.jubasbackend.controller.response.FeedbackResponse;
import com.jubasbackend.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Appointments")
@RequiredArgsConstructor
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService service;

    @Operation(summary = "Buscar agendamentos.", responses = {
            @ApiResponse(responseCode = "200", description = "Buscar realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agendamentos.")
    })
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAppointment(
            @RequestParam @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date) {
            return ResponseEntity.ok(service.getAppointments(date));
    }

    @Operation(summary = "Buscar agendamento.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agendamento.", content = @Content)
    })
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(service.getAppointment(appointmentId));
    }

    @Operation(summary = "Buscar agendamentos pelo id do usuário", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agendamentos", content = @Content)
    })
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getAppointmentsByUser(userId));
    }

    @Operation(summary = "Cadastrar novo agendamento.", responses = {
            @ApiResponse(responseCode = "201", description = "Agendamento realizado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Especialidade já agendada para o cliente."),
            @ApiResponse(responseCode = "404", description = "Cliente, funcionário e/ou especialidade não cadastrada."),
            @ApiResponse(responseCode = "409", description = "Conflito de horário."),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar o agendamento.")
    })
    @PostMapping
    public ResponseEntity<Void> createAppointment(@RequestBody AppointmentRequest request) {
        var createdAppointment = service.createAppointment(request);
        return ResponseEntity.created(URI.create("/appointments/" + createdAppointment.getId())).build();
    }

    @Operation(summary = "Alterar agendamento.", responses = {
            @ApiResponse(responseCode = "200", description = "Alteração realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao alterar o agendamento.")
    })
    @PatchMapping("/{appointmentId}")
    public ResponseEntity<Void> updateAppointment(@PathVariable UUID appointmentId,
                                                  @RequestBody AppointmentRequest request) {
        service.updateAppointment(appointmentId, request);
        return ResponseEntity.noContent().build();
    }

}
