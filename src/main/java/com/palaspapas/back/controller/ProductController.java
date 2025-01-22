package com.palaspapas.back.controller;


import com.palaspapas.back.domain.Product;
import com.palaspapas.back.dto.mapper.IProductDtoMapper;
import com.palaspapas.back.dto.request.ProductRequest;
import com.palaspapas.back.dto.response.ProductResponse;
import com.palaspapas.back.service.interfaces.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/v1/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Products", description = "API para gestión de productos y recetas")
@SecurityRequirement(name = "Bearer Authentication")
public class ProductController {

    private final IProductService productService;
    private final IProductDtoMapper productDtoMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        log.debug("Crear producto request recibido: {}", request.getName());
        Product product = productDtoMapper.toDomain(request);
        Product created = productService.create(product);
        return new ResponseEntity<>(productDtoMapper.toResponse(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar producto existente")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        Product product = productDtoMapper.toDomain(request);
        Product updated = productService.update(id, product);
        return ResponseEntity.ok(productDtoMapper.toResponse(updated));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable Long id,
            @Parameter(description = "Incluir detalles de la receta")
            @RequestParam(defaultValue = "false") boolean includeRecipe) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(productDtoMapper.toResponse(product));
    }

    @GetMapping
    @Operation(summary = "Listar productos")
    public ResponseEntity<List<ProductResponse>> findAll(
            @Parameter(description = "ID de la categoría")
            @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Solo productos con stock disponible")
            @RequestParam(required = false) Boolean available) {

        List<Product> products;
        if (categoryId != null) {
            products = productService.findByCategoryId(categoryId);
        } else {
            products = productService.findAll();
        }

        List<ProductResponse> response = products.stream()
                .map(productDtoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar producto")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cambiar estado del producto")
    public ResponseEntity<ProductResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam Boolean status) {
        Product product = productService.changeStatus(id, status);
        return ResponseEntity.ok(productDtoMapper.toResponse(product));
    }

    @GetMapping("/{id}/stock-availability")
    @Operation(summary = "Verificar disponibilidad de stock")
    public ResponseEntity<Boolean> checkStockAvailability(@PathVariable Long id) {
        boolean hasStock = productService.hasAvailableStock(id);
        return ResponseEntity.ok(hasStock);
    }
}