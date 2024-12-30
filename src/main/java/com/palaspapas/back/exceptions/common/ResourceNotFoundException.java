package com.palaspapas.back.exceptions.common;

import com.palaspapas.back.exceptions.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) {

        super(message, "NO ENCONTRADO", HttpStatus.NOT_FOUND);
    }
}