package com.palaspapas.back.services;

import com.palaspapas.back.model.dto.request.inventory.InventoryMovementRequest;
import com.palaspapas.back.model.dto.response.inventory.InventoryMovementResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryMovementService {
    InventoryMovementResponse createMovement(InventoryMovementRequest request);
    List<InventoryMovementResponse> findByIngredient(Long ingredientId);
    List<InventoryMovementResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
