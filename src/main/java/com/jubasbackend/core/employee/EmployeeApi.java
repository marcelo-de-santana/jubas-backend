package com.jubasbackend.core.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jubasbackend.core.employee.dto.EmployeeRequest;
import com.jubasbackend.core.employee.dto.EmployeeResponse;
import com.jubasbackend.core.working_hour.dto.ScheduleTimeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Employees")
@RequestMapping("/employees")
public interface EmployeeApi {

    @Operation(summary = "Buscar todos os funcionários cadastrados.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar funcionários.", content = @Content),
    })
    @GetMapping
    ResponseEntity<List<EmployeeResponse>> findEmployees();

    @Operation(summary = "Buscar funcionário por id.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Funcionário não cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar funcionário.", content = @Content)
    })
    @GetMapping("/{employeeId}")
    ResponseEntity<EmployeeResponse> findEmployee(@PathVariable UUID employeeId);

    @Operation(summary = "Buscar agenda de atendimento do funcionário.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Funcionário não cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar a agenda do funcionário.", content = @Content)
    })
    @GetMapping("/{employeeId}/appointments")
    ResponseEntity<List<? extends ScheduleTimeResponse>> findAppointmentsByEmployee(
            @PathVariable UUID employeeId,
            @JsonFormat(pattern = "yyyy-MM-dd")
            @RequestParam
            Optional<LocalDate> date);

    @Operation(summary = "Cadastrar funcionário.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros errados."),
            @ApiResponse(responseCode = "401", description = "Perfil já está em uso"),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar funcionário.")
    })
    @PostMapping
    ResponseEntity<Void> createEmployee(@RequestBody @Valid EmployeeRequest request);

    @Operation(summary = "Adicionar especialidades.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Especialidades adicionadas com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros errados."),
            @ApiResponse(responseCode = "401", description = "Funcionário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao adicionar especialidades.")
    })
    @PostMapping("/{employeeId}/specialties")
    ResponseEntity<Void> addSpecialties(
            @PathVariable UUID employeeId,
            @RequestBody @Valid @NotNull List<UUID> specialties);

    @Operation(summary = "Atualizar jornada de trabalho.", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Associação atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetro errado."),
            @ApiResponse(responseCode = "401", description = "Funcionário não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar associação.")
    })
    @PatchMapping("/{employeeId}/working-hours")
    ResponseEntity<Void> updateWorkingHour(
            @PathVariable UUID employeeId,
            @RequestBody @Valid @NotNull UUID workingHourId);


}
