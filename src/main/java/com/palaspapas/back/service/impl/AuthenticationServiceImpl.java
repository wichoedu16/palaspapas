package com.palaspapas.back.service.impl;

import com.palaspapas.back.dto.request.AuthenticationRequest;
import com.palaspapas.back.dto.request.RegisterRequest;
import com.palaspapas.back.dto.response.AuthenticationResponse;
import com.palaspapas.back.exception.BusinessException;
import com.palaspapas.back.persistence.entities.RoleEntity;
import com.palaspapas.back.persistence.entities.UserEntity;
import com.palaspapas.back.persistence.mappers.IUserMapper;
import com.palaspapas.back.persistence.repositories.IRoleRepository;
import com.palaspapas.back.persistence.repositories.IUserRepository;
import com.palaspapas.back.security.jwt.JwtService;
import com.palaspapas.back.security.jwt.UserDetailsImpl;
import com.palaspapas.back.service.interfaces.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // Validaciones de negocio
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        // Obtener el rol por defecto (asumiendo que tienes un rol USER por defecto)
        RoleEntity defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new BusinessException("Default role not found"));

        // Crear el usuario
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setEmail(request.getEmail());
        userEntity.setRole(defaultRole);
        userEntity.setStatus(true);

        UserEntity savedUser = userRepository.save(userEntity);

        // Generar token
        UserDetailsImpl userDetails = new UserDetailsImpl(savedUser);
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Autenticar usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Buscar usuario y generar token
        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("User not found"));

        UserDetailsImpl userDetails = new UserDetailsImpl(userEntity);
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(String token) {
        String username = jwtService.extractUsername(token);
        if (username != null) {
            UserEntity userEntity = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BusinessException("User not found"));

            UserDetailsImpl userDetails = new UserDetailsImpl(userEntity);
            if (jwtService.isTokenValid(token, userDetails)) {
                String newToken = jwtService.generateToken(userDetails);
                return AuthenticationResponse.builder()
                        .token(newToken)
                        .build();
            }
        }
        throw new BusinessException("Invalid refresh token");
    }
}