package com.jubasbackend.core.category;

import com.jubasbackend.core.category.dto.CategoryResponse;
import com.jubasbackend.core.category.dto.CategorySpecialtyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category")
public interface CategoryApi {

    @Operation(summary = "Buscar todas as categorias.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar categorias.", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<CategoryResponse>> findCategories();

    @Operation(summary = "Buscar todas as categorias e especialidades associadas.", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar categorias.", content = @Content)
    })
    @GetMapping("/specialties")
    ResponseEntity<List<CategorySpecialtyResponse>> findCategoriesAndSpecialties();

    @Operation(summary = "Cadastrar nova categoria.", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Categoria já está cadastrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar categoria.", content = @Content)
    })
    @PostMapping
    ResponseEntity<CategoryResponse> createCategory(@RequestBody String name);

    @Operation(summary = "Atualizar categoria.", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria atualizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não existe."),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar categoria.")
    })
    @PatchMapping("/{categoryId}")
    ResponseEntity<Void> updateCategory(@PathVariable Short categoryId, @RequestBody String name);

    @Operation(summary = "Excluir categoria.", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não existe."),
            @ApiResponse(responseCode = "500", description = "Erro ao excluir categoria.")
    })
    @DeleteMapping("/{categoryId}")
    ResponseEntity<Void> deleteCategory(@PathVariable Short categoryId);
}
