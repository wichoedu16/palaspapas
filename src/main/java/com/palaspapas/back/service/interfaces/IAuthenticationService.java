package com.palaspapas.back.service.interfaces;


import com.palaspapas.back.dto.request.AuthenticationRequest;
import com.palaspapas.back.dto.request.RegisterRequest;
import com.palaspapas.back.dto.response.AuthenticationResponse;

public interface IAuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}