package com.palaspapas.back.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeItem {
    private Long id;
    private Ingredient ingredient;
    private BigDecimal quantity;
    private String unitMeasure;
}