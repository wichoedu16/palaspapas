package com.palaspapas.back.model.dto.response.dish;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DishIngredientResponse {
    private Long id;
    private Long ingredientId;
    private String ingredientName;
    private String measurementUnit;  // Aquí almacenaremos el símbolo
    private BigDecimal quantity;
    private BigDecimal unitCost;
    private BigDecimal totalCost;
}