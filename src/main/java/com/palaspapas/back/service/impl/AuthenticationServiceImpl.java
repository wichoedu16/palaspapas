package com.palaspapas.back.service.impl;

import com.palaspapas.back.dto.request.AuthenticationRequest;
import com.palaspapas.back.dto.request.RegisterRequest;
import com.palaspapas.back.dto.response.AuthenticationResponse;
import com.palaspapas.back.exception.BadRequestException;
import com.palaspapas.back.persistence.entities.UserEntity;
import com.palaspapas.back.persistence.repositories.IUserRepository;
import com.palaspapas.back.security.jwt.JwtService;
import com.palaspapas.back.service.interfaces.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }

        var user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .role(request.getRole() != null ? request.getRole() : "USER")
                .status(request.getStatus())
                .build();

        userRepository.save(user);

        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.debug("Iniciando proceso de autenticación");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            log.debug("Credenciales validadas correctamente");

            UserEntity user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> {
                        log.error("Usuario no encontrado: {}", request.getUsername());
                        return new UsernameNotFoundException("User not found");
                    });

            log.debug("Usuario encontrado, generando token");
            String token = jwtService.generateToken(user);
            log.debug("Token generado exitosamente");

            return AuthenticationResponse.builder()
                    .token(token)
                    .build();
        } catch (Exception e) {
            log.error("Error en el proceso de autenticación: {}", e.getMessage(), e);
            throw e;
        }
    }
}
