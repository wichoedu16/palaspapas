package com.palaspapas.back.model.dto.request.dish;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DishIngredientRequest {
    @NotNull(message = "El ingrediente es requerido")
    private Long ingredientId;

    @NotNull(message = "La cantidad es requerida")
    @DecimalMin(value = "0.0", inclusive = false, message = "La cantidad debe ser mayor que cero")
    private BigDecimal quantity;
}