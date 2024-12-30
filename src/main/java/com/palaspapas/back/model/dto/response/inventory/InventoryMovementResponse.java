package com.palaspapas.back.model.dto.response.inventory;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class InventoryMovementResponse {
    private Long id;
    private Long ingredientId;
    private String ingredientName;
    private String type;
    private BigDecimal quantity;
    private BigDecimal unitCost;
    private String reason;
    private String referenceType;
    private Long referenceId;
    private String notes;
    private LocalDateTime createdAt;
}