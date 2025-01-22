package com.palaspapas.back.exception.handler;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> errors;

    public ValidationErrorResponse(String code, String message, Map<String, String> errors, LocalDateTime timestamp) {
        super(code, message, timestamp);
        this.errors = errors;
    }
}