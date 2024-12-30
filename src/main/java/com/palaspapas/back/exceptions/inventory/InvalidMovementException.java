package com.palaspapas.back.exceptions.inventory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMovementException extends InventoryException {
    public InvalidMovementException(String message) {
        super(message);
    }
}