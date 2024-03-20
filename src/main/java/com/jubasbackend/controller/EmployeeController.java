package com.jubasbackend.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.controller.request.EmployeeRequest;
import com.jubasbackend.controller.response.EmployeeResponse;
import com.jubasbackend.service.EmployeeService;
import com.jubasbackend.controller.response.ScheduleTimeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Tag(name = "Employees")
@RequiredArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService service;

    @Operation(summary = "Buscar todos os funcionários cadastrados.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar funcionários.", content = @Content),
    })
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> findEmployees() {
        return ResponseEntity.ok(service.findEmployees());
    }

    @Operation(summary = "Buscar funcionário por id.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Funcionário não cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar funcionário.", content = @Content)
    })
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> findEmployee(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(service.findEmployee(employeeId));
    }

    @Operation(summary = "Buscar agenda de atendimento do funcionário.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Funcionário não cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar a agenda do funcionário.", content = @Content)
    })
    @GetMapping("/{employeeId}/appointments")
    public ResponseEntity<List<? extends ScheduleTimeResponse>> findAppointmentsByEmployee(
            @PathVariable UUID employeeId,
            @RequestParam(required = false) @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return ResponseEntity.ok(service.findAppointmentsByEmployee(employeeId, date));
    }

    @Operation(summary = "Cadastrar funcionário.", responses = {
            @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros errados."),
            @ApiResponse(responseCode = "401", description = "Perfil já está em uso"),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar funcionário.")
    })
    @PostMapping
    public ResponseEntity<Void> createEmployee(@RequestBody @Valid EmployeeRequest request) {
        var createdEmployee = service.createEmployee(request);
        return ResponseEntity.created(URI.create("/employees/" + createdEmployee.id())).build();
    }

    @Operation(summary = "Atualizar funcionário.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Funcionário atualizado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Parâmetros errados."),
                    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro ao atualizar funcionário.")
            },
            description = "Lógica das especialidades: Remove se já associada, adiciona caso contrário.")

    @PatchMapping("/{employeeId}")
    public ResponseEntity<Void> updateEmployee(
            @PathVariable UUID employeeId,
            @RequestBody @Valid EmployeeRequest request) {
        service.updateEmployee(employeeId, request);
        return ResponseEntity.noContent().build();
    }

}

