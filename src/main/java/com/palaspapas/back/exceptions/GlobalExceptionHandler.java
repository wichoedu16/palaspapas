package com.palaspapas.back.exceptions;

import com.palaspapas.back.exceptions.category.CategoryException;
import com.palaspapas.back.exceptions.common.ResourceNotFoundException;
import com.palaspapas.back.exceptions.common.ValidationException;
import com.palaspapas.back.exceptions.inventory.InsufficientStockException;
import com.palaspapas.back.exceptions.inventory.InvalidMovementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones que centraliza todo el manejo de errores de la aplicación.
 * Asegura una respuesta consistente para todos los tipos de errores.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        log.error("Recurso no encontrado: {}", ex.getMessage());
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException ex) {
        log.error("Stock insuficiente: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("availableStock", ex.getAvailableStock());
        details.put("requestedQuantity", ex.getRequestedQuantity());

        return buildErrorResponse(ex, details);
    }

    @ExceptionHandler(InvalidMovementException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMovement(BaseException ex) {
        log.error("Movimiento inválido: {}", ex.getMessage());
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<ErrorResponse> handleCategoryException(CategoryException ex) {
        log.error("Error en categoría: {}", ex.getMessage());
        return buildErrorResponse(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                details.put(error.getField(), error.getDefaultMessage())
        );

        ValidationException validationException = new ValidationException("Error de validación");
        return buildErrorResponse(validationException, details);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(Exception ex) {
        log.error("Error no manejado", ex);
        BaseException baseException = new BaseException("Error interno del servidor", "INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR) {};
        return buildErrorResponse(baseException);
    }

    // Método principal para construir la respuesta de error
    private ResponseEntity<ErrorResponse> buildErrorResponse(BaseException ex) {
        return buildErrorResponse(ex, new HashMap<>());
    }

    // Sobrecarga del método para incluir detalles adicionales
    private ResponseEntity<ErrorResponse> buildErrorResponse(BaseException ex, Map<String, Object> details) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ex.getStatus().value())
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}