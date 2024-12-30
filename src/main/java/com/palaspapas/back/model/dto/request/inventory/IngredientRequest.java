package com.palaspapas.back.model.dto.request.inventory;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO para la creación o actualización de ingredientes
 */
@Data
@Builder
public class IngredientRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @NotNull(message = "El stock actual es requerido")
    @DecimalMin(value = "0.0", message = "El stock actual no puede ser negativo")
    private BigDecimal currentStock;

    @NotNull(message = "El stock mínimo es requerido")
    @DecimalMin(value = "0.0", message = "El stock mínimo no puede ser negativo")
    private BigDecimal minimumStock;

    @NotNull(message = "El costo unitario es requerido")
    @DecimalMin(value = "0.0", message = "El costo unitario no puede ser negativo")
    private BigDecimal currentUnitCost;

    @DecimalMin(value = "0.0", message = "El precio de venta no puede ser negativo")
    private BigDecimal sellingPrice;

    private Long providerId;
}