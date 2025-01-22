package com.palaspapas.back.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecipeItemRequest {
    @NotNull(message = "El ingrediente es requerido")
    private Long ingredientId;

    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private BigDecimal quantity;

    @NotNull(message = "La unidad de medida es requerida")
    private String unitMeasure;
}