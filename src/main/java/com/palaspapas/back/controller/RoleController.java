package com.palaspapas.back.controller;

import com.palaspapas.back.domain.Role;
import com.palaspapas.back.dto.mapper.IRoleDtoMapper;
import com.palaspapas.back.dto.request.RoleRequest;
import com.palaspapas.back.dto.response.RoleResponse;
import com.palaspapas.back.service.interfaces.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "API para gestión de roles")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class RoleController {
    private final IRoleService roleService;
    private final IRoleDtoMapper roleMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Crear nuevo rol", description = "Creates un nuevo rol con sus permisos")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
        log.debug("Crear rol request recibido: {}", request);
        Role role  = roleMapper.toDomain(request);
        Role createdRole = roleService.createRole(role);
        return  new ResponseEntity<>(roleMapper.toResponse(createdRole),
                HttpStatus.CREATED);
    }
}