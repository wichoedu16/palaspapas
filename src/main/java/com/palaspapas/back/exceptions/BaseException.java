package com.palaspapas.back.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Excepción base para todas las excepciones personalizadas de la aplicación.
 * Proporciona funcionalidad común para todas nuestras excepciones.
 */
@Getter
public abstract class BaseException extends RuntimeException {
    private final String code;
    private final HttpStatus status;

    protected BaseException(String message, String code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    protected BaseException(String message, String code, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.status = status;
    }
}