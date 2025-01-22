package com.palaspapas.back.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class IngredientResponse {
    private Long id;
    private String name;
    private String description;
    private CategoryResponse category;
    private String unitMeasure;
    private BigDecimal stock;
    private BigDecimal minimumStock;
    private BigDecimal cost;
    private Boolean isForKitchen;
    private Boolean isAddition;
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}