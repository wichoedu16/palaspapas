package com.palaspapas.back.controller;

import com.palaspapas.back.dto.request.AuthenticationRequest;
import com.palaspapas.back.dto.request.RegisterRequest;
import com.palaspapas.back.dto.response.AuthenticationResponse;
import com.palaspapas.back.service.interfaces.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthenticationController {

    private final IAuthenticationService authenticationService;
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Servicio activo!!");
    }
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        log.debug("Received registration request for user: {}", request.getUsername());
        var response = authenticationService.register(request);
        log.debug("Registration successful for user: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate( @Valid @RequestBody AuthenticationRequest request) {
        log.debug("Recibida solicitud de autenticación para usuario: {}",
                request.getUsername());
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}