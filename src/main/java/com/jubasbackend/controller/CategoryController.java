package com.jubasbackend.controller;

import com.jubasbackend.controller.request.CategoryRequest;
import com.jubasbackend.controller.response.CategoryResponse;
import com.jubasbackend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@Tag(name = "Categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    @Operation(summary = "Buscar todas as categorias.", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar categorias.", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<? extends CategoryResponse>> findCategories(
            @RequestParam(required = false, defaultValue = "false") boolean specialties) {
        return ResponseEntity.ok(specialties ? service.findCategoriesWithSpecialties() : service.findCategories());
    }

    @Operation(summary = "Cadastrar nova categoria.", responses = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Categoria já está cadastrada."),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar categoria.")
    })
    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CategoryRequest request) {
        var categoryCreated = service.createCategory(request.name());
        return ResponseEntity.created(URI.create("/categories/" + categoryCreated.getId())).build();
    }

    @Operation(summary = "Atualizar categoria.", responses = {
            @ApiResponse(responseCode = "204", description = "Categoria atualizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não existe."),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar categoria.")
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Short categoryId, @RequestBody CategoryRequest request) {
        service.updateCategory(categoryId, request.name());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir categoria.", responses = {
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não existe."),
            @ApiResponse(responseCode = "500", description = "Erro ao excluir categoria.")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Short categoryId) {
        service.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
