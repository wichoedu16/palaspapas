package com.palaspapas.back.service.interfaces;


import com.palaspapas.back.controller.dto.request.AuthenticationRequest;
import com.palaspapas.back.controller.dto.request.RegisterRequest;
import com.palaspapas.back.controller.dto.response.AuthenticationResponse;

public interface IAuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}