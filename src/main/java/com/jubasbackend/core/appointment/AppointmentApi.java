package com.jubasbackend.core.appointment;

import com.jubasbackend.core.appointment.dto.AppointmentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface AppointmentApi {
    @Operation(summary = "Buscar parâmetros de atendimento do funcionário.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Funcionário não cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar parâmetros do funcionário.", content = @Content)
    })
    @GetMapping("/{employeeId}/schedule")
    ResponseEntity<AppointmentResponse> findScheduleByEmployee(@PathVariable UUID employeeId);
}
