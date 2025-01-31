package com.palaspapas.back.controller;

import com.palaspapas.back.domain.Category;
import com.palaspapas.back.dto.mapper.ICategoryDtoMapper;
import com.palaspapas.back.dto.request.CategoryRequest;
import com.palaspapas.back.dto.response.CategoryResponse;
import com.palaspapas.back.service.interfaces.ICategoryService;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "API para gestión de categorías")
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryController {

    private final ICategoryService categoryService;
    private final ICategoryDtoMapper categoryMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nueva categoría")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        log.debug("Crear categoría request recibido: {}", request);
        Category category = categoryMapper.toDomain(request);
        Category createdCategory = categoryService.create(category);
        return new ResponseEntity<>(categoryMapper.toResponse(createdCategory),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar categoría existente")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        log.debug("Actualizar categoría request recibido para id: {}", id);
        Category category = categoryMapper.toDomain(request);
        Category updatedCategory = categoryService.update(id, category);
        return ResponseEntity.ok(categoryMapper.toResponse(updatedCategory));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        log.debug("Buscar categoría por id: {}", id);
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(categoryMapper.toResponse(category));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las categorías")
    public ResponseEntity<List<CategoryResponse>> findAll() {
        log.debug("Obtener todas las categorías");
        List<Category> categories = categoryService.findAll();
        List<CategoryResponse> response = categories.stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar categoría")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Eliminar categoría con id: {}", id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cambiar estado de categoría")
    public ResponseEntity<CategoryResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam Boolean status) {
        log.debug("Cambiar estado de categoría id: {} a: {}", id, status);
        Category category = categoryService.changeStatus(id, status);
        return ResponseEntity.ok(categoryMapper.toResponse(category));
    }
}
