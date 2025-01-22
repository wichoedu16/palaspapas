package com.palaspapas.back.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RecipeItemResponse {
    private Long id;
    private IngredientResponse ingredient;
    private BigDecimal quantity;
    private String unitMeasure;
}