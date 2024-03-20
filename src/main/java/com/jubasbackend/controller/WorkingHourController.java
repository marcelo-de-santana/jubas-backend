package com.jubasbackend.controller;

import com.jubasbackend.service.WorkingHourService;
import com.jubasbackend.controller.request.WorkingHourRequest;
import com.jubasbackend.controller.response.WorkingHourResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "WorkingHours")
@RequiredArgsConstructor
@RestController
@RequestMapping("/working-hours")
public class WorkingHourController {

    private final WorkingHourService service;

    @Operation(summary = "Buscar jornadas de trabalho.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar jornadas de trabalho.", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<WorkingHourResponse>> findWorkingHours() {
        return ResponseEntity.ok(service.findWorkingHours());
    }

    @Operation(summary = "Cadastrar nova jornada de trabalho.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro realizado com sucesso.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Jornada de trabalho já cadastrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar jornada de trabalho.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> createWorkingHour(@RequestBody WorkingHourRequest request) {
        var createdWorkingHour = service.createWorkingHour(request);
        return ResponseEntity.created(URI.create("working-hours/" + createdWorkingHour.id())).build();
    }

    @Operation(summary = "Atualizar jornada de trabalho.", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Jornada de trabalho atualizada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Registro não encontrado.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar jornada de trabalho.", content = @Content)
    })
    @PutMapping("/{workingHourId}")
    public ResponseEntity<Void> updateWorkingHour(@PathVariable UUID workingHourId, @RequestBody WorkingHourRequest request) {
        service.updateWorkingHour(workingHourId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir jornada de trabalho.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exclusão realizada com sucesso.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao excluir jornada de trabalho.", content = @Content)
    })
    @DeleteMapping("/{workingHourId}")
    public ResponseEntity<Void> deleteWorkingHour(@PathVariable UUID workingHourId) {
        service.deleteWorkingHour(workingHourId);
        return ResponseEntity.noContent().build();
    }
}
