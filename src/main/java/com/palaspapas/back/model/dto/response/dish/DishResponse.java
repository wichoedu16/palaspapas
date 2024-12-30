package com.palaspapas.back.model.dto.response.dish;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DishResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal cost;
    private CategoryResponse category;
    private List<DishIngredientResponse> ingredients;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}