package com.palaspapas.back.model.dto.response.sale;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderDetailResponse {
    private Long id;
    private Long dishId;
    private String dishName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String notes;
    // Si necesitas la unidad de medida, agrégala aquí
    private String measurementUnitSymbol;
}