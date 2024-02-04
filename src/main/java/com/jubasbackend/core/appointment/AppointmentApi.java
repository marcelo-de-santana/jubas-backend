package com.jubasbackend.core.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.core.appointment.dto.AppointmentCreateRequest;
import com.jubasbackend.core.workingHour.dto.ScheduleTime;
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

    @Operation(summary = "Cadastrar novo agendamento.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agendamento realizado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Especialidade já agendada para o cliente."),
            @ApiResponse(responseCode = "404", description = "Cliente, funcionário e/ou especialidade não cadastrada."),
            @ApiResponse(responseCode = "409", description = "Conflito de horário."),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar o agendamento.")
    })
    @PostMapping
    ResponseEntity<Void> createAppointment(@Valid @RequestBody AppointmentCreateRequest request);
}
