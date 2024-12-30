package com.palaspapas.back.model.dto.request.sale;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailRequest {
    @NotNull(message = "El plato es requerido")
    private Long dishId;

    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;

    private String notes;
}