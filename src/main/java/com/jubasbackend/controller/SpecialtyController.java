package com.jubasbackend.controller;

import com.jubasbackend.controller.request.SpecialtyRequest;
import com.jubasbackend.controller.response.SpecialtyResponse;
import com.jubasbackend.service.SpecialtyService;
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

@Tag(name = "Specialties")
@RequiredArgsConstructor
@RestController
@RequestMapping("/specialties")
public class SpecialtyController {

    private final SpecialtyService service;

    @Operation(summary = "Buscar especialidades.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar especialidades.", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<SpecialtyResponse>> findSpecialties() {
        return ResponseEntity.ok(service.findSpecialties());
    }

    @Operation(summary = "Cadastrar especialidade.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Especialidade cadastrada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Especialidade já cadastrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar especialidade.", content = @Content)
    })
    @PostMapping
    ResponseEntity<Void> createSpecialty(@RequestBody SpecialtyRequest request) {
        var specialtyCreated = service.createSpecialty(request);
        return ResponseEntity.created(URI.create("/specialties/" + specialtyCreated.id())).build();
    }

    @Operation(summary = "Atualizar especialidade.", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Especialidade atualizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Especialidade não cadastrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar especialidade.", content = @Content)
    })
    @PatchMapping("/{specialtyId}")
    ResponseEntity<Void> updateSpecialty(@PathVariable UUID specialtyId, @RequestBody SpecialtyRequest request) {
        service.updateSpecialty(specialtyId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir especialidade.", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Especialidade atualizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Especialidade não encontrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao deletar especialidade.", content = @Content)
    })
    @DeleteMapping("/{specialtyId}")
    ResponseEntity<Void> deleteSpecialty(@PathVariable UUID specialtyId) {
        service.deleteSpecialty(specialtyId);
        return ResponseEntity.noContent().build();
    }
}
