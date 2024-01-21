package com.jubasbackend.api;

import com.jubasbackend.api.dto.request.EmployeeCreateRequest;
import com.jubasbackend.api.dto.response.EmployeeWorkingHourSpecialtiesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Employee")
public interface EmployeeApi {

    @Operation(summary = "Buscar todos os parâmetros do funcionário.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Funcionário não cadastrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar parâmetros do funcionário.", content = @Content)
    })
    @GetMapping("/{employeeId}")
    ResponseEntity<EmployeeWorkingHourSpecialtiesResponse> findEmployee(@PathVariable UUID employeeId);

    @Operation(summary = "Cadastrar funcionário.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Parâmetros errados."),
            @ApiResponse(responseCode = "401", description = "Perfil já está em uso"),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar funcionário.")
    })
    @PostMapping
    ResponseEntity<Void> createEmployee(@RequestBody @Valid EmployeeCreateRequest request);

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
    @PatchMapping("/{employeeId}/working-hour")
    ResponseEntity<Void> updateWorkingHour(
            @PathVariable UUID employeeId,
            @RequestBody @Valid @NotNull UUID workingHourId);


}
