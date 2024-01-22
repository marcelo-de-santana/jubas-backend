package com.jubasbackend.api;

import com.jubasbackend.api.dto.request.WorkingHourRequest;
import com.jubasbackend.api.dto.response.WorkingHourResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "WorkingHour")
public interface WorkingHourApi {

    @Operation(summary = "Buscar jornadas de trabalho.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar jornadas de trabalho.", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<WorkingHourResponse>> findWorkingHours();

    @Operation(summary = "Cadastrar nova jornada de trabalho.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro realizado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Jornada de trabalho já cadastrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar jornada de trabalho.", content = @Content)
    })
    @PostMapping
    ResponseEntity<Void> createWorkingHour(@RequestBody WorkingHourRequest request);

    @Operation(summary = "Atualizar jornada de trabalho.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Jornada de trabalho atualizada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar jornada de trabalho.", content = @Content)
    })
    @PutMapping("/{workingHourId}")
    ResponseEntity<Void> updateWorkingHour(@PathVariable UUID workingHourId, @RequestBody WorkingHourRequest request);

    @Operation(summary = "Excluir jornada de trabalho.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exclusão realizada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao excluir jornada de trabalho.", content = @Content)
    })
    @DeleteMapping("/{workingHourId}")
    ResponseEntity<Void> deleteWorkingHour(@PathVariable UUID workingHourId);
}
