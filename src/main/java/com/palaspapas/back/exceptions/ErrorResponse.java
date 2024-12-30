package com.palaspapas.back.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Estructura estandarizada para todas las respuestas de error de la API.
 * Asegura que todos los errores se devuelvan en un formato consistente.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String code;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, Object> details;
}