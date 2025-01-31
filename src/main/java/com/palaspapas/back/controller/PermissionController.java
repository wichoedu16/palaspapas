package com.palaspapas.back.controller;

import com.palaspapas.back.domain.Permission;
import com.palaspapas.back.dto.mapper.IPermissionDtoMapper;
import com.palaspapas.back.dto.request.PermissionRequest;
import com.palaspapas.back.dto.response.PermissionResponse;
import com.palaspapas.back.service.interfaces.IPermissionService;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "API para gestión de permisos")
@SecurityRequirement(name = "Bearer Authentication")
public class PermissionController {

    private final IPermissionService permissionService;
    private final IPermissionDtoMapper permissionMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Crear nuevo permiso")
    public ResponseEntity<PermissionResponse> create(@Valid @RequestBody PermissionRequest request) {
        log.debug("Crear permiso request recibido: {}", request);
        Permission permission = permissionMapper.toDomain(request);
        Permission createdPermission = permissionService.create(permission);
        return new ResponseEntity<>(permissionMapper.toResponse(createdPermission),
                HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @Operation(summary = "Get all permissions")
    public ResponseEntity<List<PermissionResponse>> findAll() {
        var permissions = permissionService.findAll();
        var response = permissions.stream()
                .map(permissionMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_READ')")
    @Operation(summary = "Get permission by id")
    public ResponseEntity<PermissionResponse> findById(@PathVariable Long id) {
        return permissionService.findById(id)
                .map(permissionMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    @Operation(summary = "Update permission")
    public ResponseEntity<PermissionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PermissionRequest request) {
        var permission = permissionMapper.toDomain(request);
        permission.setId(id);
        var updatedPermission = permissionService.update(permission);
        return ResponseEntity.ok(permissionMapper.toResponse(updatedPermission));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_DELETE')")
    @Operation(summary = "Delete permission")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}