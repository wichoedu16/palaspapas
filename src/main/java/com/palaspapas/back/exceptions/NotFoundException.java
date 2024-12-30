package com.palaspapas.back.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando un recurso solicitado no se encuentra en el sistema.
 * Por ejemplo, cuando se busca un ingrediente que no existe.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(message, "ERROR-404", HttpStatus.NOT_FOUND);
    }
}