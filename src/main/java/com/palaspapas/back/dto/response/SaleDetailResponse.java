package com.palaspapas.back.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDetailResponse {
    private Long id;
    private ProductResponse product;
    private IngredientResponse ingredient;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String notes;
    private Boolean isAdditional;
    private Long parentDetailId;
}