package com.palaspapas.back.model.dto.request.inventory;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class InventoryMovementRequest {
    @NotNull(message = "El ingrediente es requerido")
    private Long ingredientId;

    @NotNull(message = "El tipo de movimiento es requerido")
    private String movementType;

    @NotNull(message = "La cantidad es requerida")
    @DecimalMin(value = "0.0", inclusive = false, message = "La cantidad debe ser mayor que cero")
    private BigDecimal quantity;

    @Size(max = 100, message = "La razón no puede exceder 100 caracteres")
    private String reason;

    private String referenceType;
    private Long referenceId;
    private String notes;
    private BigDecimal sellingPrice;
}
