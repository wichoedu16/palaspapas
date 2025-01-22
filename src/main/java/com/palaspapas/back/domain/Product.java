package com.palaspapas.back.domain;

import com.palaspapas.back.domain.common.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseDomain {
    private String name;
    private String description;
    private Category category;
    private BigDecimal price;
    private List<RecipeItem> recipeItems;
}
