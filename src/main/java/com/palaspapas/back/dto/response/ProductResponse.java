package com.palaspapas.back.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private CategoryResponse category;
    private BigDecimal price;
    private List<RecipeItemResponse> recipeItems;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}