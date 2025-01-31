package com.palaspapas.back.controller;

import com.palaspapas.back.domain.User;
import com.palaspapas.back.dto.mapper.IUserDtoMapper;
import com.palaspapas.back.dto.request.UserRequest;
import com.palaspapas.back.dto.response.UserResponse;
import com.palaspapas.back.service.interfaces.IUserService;
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
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API para gestión de usuarios")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final IUserService userService;
    private final IUserDtoMapper userMapper;

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        log.debug("Crear usuario request recibido: {}", request);
        User user = userMapper.toDomain(request);
        User createdUser = userService.create(user);
        return new ResponseEntity<>(userMapper.toResponse(createdUser),
                HttpStatus.CREATED);
    }
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        var users = userService.findAll();
        return ResponseEntity.ok(users.stream()
                .map(userMapper::toResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Assign role to user")
    public ResponseEntity<UserResponse> assignRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        var updatedUser = userService.assignRole(userId, roleId);
        return ResponseEntity.ok(userMapper.toResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}