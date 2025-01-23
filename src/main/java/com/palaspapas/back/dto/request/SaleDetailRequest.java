package com.palaspapas.back.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Detalle de venta")
public class SaleDetailRequest {
    private Long productId;
    private Long ingredientId;

    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private BigDecimal quantity;

    private String notes;
    private Boolean isAdditional;
    private Long parentDetailId;
}