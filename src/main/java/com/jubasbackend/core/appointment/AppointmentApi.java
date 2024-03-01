package com.jubasbackend.core.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.appointment.dto.AppointmentResponse;
import com.jubasbackend.core.appointment.dto.AppointmentUpdateRequest;
import com.jubasbackend.core.appointment.dto.ScheduleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Appointments")
@RequestMapping("/appointments")
public interface AppointmentApi {

    @Operation(summary = "Buscar agendamentos.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agendamentos.", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<ScheduleResponse>> findAppointments(
            @JsonFormat(pattern = "yyyy-MM-dd")
            @RequestParam("date") Optional<LocalDate> date,
            @RequestParam Optional<UUID> specialtyId
    );

    @Operation(summary = "Buscar agendamento.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar agendamento.", content = @Content)
    })
    @GetMapping("/{appointmentId}")
    ResponseEntity<AppointmentResponse> findAppointment(@PathVariable UUID appointmentId);

    @Operation(summary = "Buscar dias de atendimento.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Nenhum dia encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar dias de atendimento.", content = @Content)
    })
    @GetMapping("/daysOfAttendance")
    ResponseEntity<List<String>> findDaysOfAttendance();

    @Operation(summary = "Cadastrar novo agendamento.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agendamento realizado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Especialidade já agendada para o cliente."),
            @ApiResponse(responseCode = "404", description = "Cliente, funcionário e/ou especialidade não cadastrada."),
            @ApiResponse(responseCode = "409", description = "Conflito de horário."),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar o agendamento.")
    })
    @PostMapping
    ResponseEntity<Void> createAppointment(@Valid @RequestBody AppointmentCreateRequest request);

    @Operation(summary = "Alterar agendamento.", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alteração realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao alterar o agendamento.")
    })
    @PatchMapping("/{appointmentId}")
    ResponseEntity<Void> updateAppointment(@PathVariable UUID appointmentId, @RequestBody AppointmentUpdateRequest request);

    @Operation(summary = "Cancelar agendamento.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cancelamento realizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao cancelar agendamento.")
    })
    @DeleteMapping("/{appointmentId}")
    ResponseEntity<Void> cancelAppointment(@PathVariable UUID appointmentId);
}
