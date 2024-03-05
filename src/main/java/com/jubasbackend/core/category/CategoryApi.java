package com.jubasbackend.core.category;

import com.jubasbackend.core.category.dto.CategoryRequest;
import com.jubasbackend.core.category.dto.CategoryResponse;
import com.jubasbackend.core.category.dto.CategorySpecialtyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categories")
@RequestMapping("/categories")
public interface CategoryApi {

    @Operation(summary = "Buscar todas as categorias.", method = "GET", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar categorias.", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<CategoryResponse>> findCategories();

    @Operation(summary = "Buscar todas as categorias e especialidades associadas.", method = "GET", responses = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro ao buscar categorias.", content = @Content)
    })
    @GetMapping("/specialties")
    ResponseEntity<List<CategorySpecialtyResponse>> findCategoriesAndSpecialties();

    @Operation(summary = "Cadastrar nova categoria.", method = "POST", responses = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Categoria já está cadastrada.", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao cadastrar categoria.", content = @Content)
    })
    @PostMapping
    ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request);

    @Operation(summary = "Atualizar categoria.", method = "PUT", responses = {
            @ApiResponse(responseCode = "204", description = "Categoria atualizada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não existe."),
            @ApiResponse(responseCode = "500", description = "Erro ao atualizar categoria.")
    })
    @PutMapping("/{categoryId}")
    ResponseEntity<Void> updateCategory(@PathVariable Short categoryId, @RequestBody CategoryRequest request);

    @Operation(summary = "Excluir categoria.", method = "PATCH", responses = {
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso."),
            @ApiResponse(responseCode = "404", description = "Categoria não existe."),
            @ApiResponse(responseCode = "500", description = "Erro ao excluir categoria.")
    })
    @DeleteMapping("/{categoryId}")
    ResponseEntity<Void> deleteCategory(@PathVariable Short categoryId);
}
