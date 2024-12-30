package com.palaspapas.back.model.dto.response.inventory;

import com.palaspapas.back.model.dto.response.provider.ProviderResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para respuestas detalladas de ingredientes
 */
@Data
@Builder
public class IngredientResponse {
    private Long id;
    private String name;
    private BigDecimal currentStock;
    private BigDecimal minimumStock;
    private BigDecimal currentUnitCost;
    private BigDecimal sellingPrice;
    private ProviderResponse provider;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}