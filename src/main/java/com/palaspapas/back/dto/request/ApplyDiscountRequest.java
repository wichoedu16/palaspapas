package com.palaspapas.back.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Solicitud para aplicar descuento")
public class ApplyDiscountRequest {
    @NotNull(message = "El descuento es requerido")
    @PositiveOrZero(message = "El descuento debe ser mayor o igual a 0")
    private BigDecimal discount;
}