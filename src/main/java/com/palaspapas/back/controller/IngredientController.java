package com.palaspapas.back.controller;


import com.palaspapas.back.domain.Ingredient;
import com.palaspapas.back.dto.mapper.IIngredientDtoMapper;
import com.palaspapas.back.dto.request.IngredientRequest;
import com.palaspapas.back.dto.response.IngredientResponse;
import com.palaspapas.back.service.interfaces.IIngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ingredients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ingredients", description = "API para gestión de ingredientes")
@SecurityRequirement(name = "Bearer Authentication")
public class IngredientController {

    private final IIngredientService ingredientService;
    private final IIngredientDtoMapper ingredientDtoMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nuevo ingrediente")
    public ResponseEntity<IngredientResponse> create(@Valid @RequestBody IngredientRequest request) {
        log.debug("Crear ingrediente request recibido: {}", request.getName());
        Ingredient ingredient = ingredientDtoMapper.toDomain(request);
        Ingredient created = ingredientService.create(ingredient);
        return new ResponseEntity<>(ingredientDtoMapper.toResponse(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar ingrediente existente")
    public ResponseEntity<IngredientResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody IngredientRequest request) {
        Ingredient ingredient = ingredientDtoMapper.toDomain(request);
        Ingredient updated = ingredientService.update(id, ingredient);
        return ResponseEntity.ok(ingredientDtoMapper.toResponse(updated));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener ingrediente por ID")
    public ResponseEntity<IngredientResponse> findById(@PathVariable Long id) {
        Ingredient ingredient = ingredientService.findById(id);
        return ResponseEntity.ok(ingredientDtoMapper.toResponse(ingredient));
    }

    @GetMapping
    @Operation(summary = "Listar todos los ingredientes")
    public ResponseEntity<List<IngredientResponse>> findAll(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean lowStock) {

        List<Ingredient> ingredients;
        if (categoryId != null) {
            ingredients = ingredientService.findByCategoryId(categoryId);
        } else if (Boolean.TRUE.equals(lowStock)) {
            ingredients = ingredientService.findLowStock();
        } else {
            ingredients = ingredientService.findAll();
        }

        List<IngredientResponse> response = ingredients.stream()
                .map(ingredientDtoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar ingrediente")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ingredientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cambiar estado del ingrediente")
    public ResponseEntity<IngredientResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam Boolean status) {
        Ingredient ingredient = ingredientService.changeStatus(id, status);
        return ResponseEntity.ok(ingredientDtoMapper.toResponse(ingredient));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Obtener ingrediente con stock bajo")
    public ResponseEntity<List<IngredientResponse>> findLowStock (@RequestParam(required = false) Boolean lowStock) {
        List<Ingredient> ingredients;
        ingredients = ingredientService.findLowStock();
        List<IngredientResponse> response = ingredients.stream()
                .map(ingredientDtoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

}