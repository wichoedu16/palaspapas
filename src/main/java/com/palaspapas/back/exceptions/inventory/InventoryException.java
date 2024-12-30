package com.palaspapas.back.exceptions.inventory;

import com.palaspapas.back.exceptions.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InventoryException extends BaseException {
    public InventoryException(String message) {
        super(message, "INVENTORY_ERROR", HttpStatus.BAD_REQUEST);
    }
}