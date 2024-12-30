package com.palaspapas.back.exceptions.inventory;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class InsufficientStockException extends InventoryException {
    private final BigDecimal availableStock;
    private final BigDecimal requestedQuantity;

    public InsufficientStockException(String message, BigDecimal availableStock, BigDecimal requestedQuantity) {
        super(message);
        this.availableStock = availableStock;
        this.requestedQuantity = requestedQuantity;
    }
}