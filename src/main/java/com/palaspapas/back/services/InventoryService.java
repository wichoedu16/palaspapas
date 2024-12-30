package com.palaspapas.back.services;

import com.palaspapas.back.model.dto.request.inventory.IngredientRequest;
import com.palaspapas.back.model.dto.response.inventory.IngredientResponse;
import com.palaspapas.back.model.dto.response.inventory.InventoryMovementResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface InventoryService {
    // Operaciones básicas de ingredientes
    IngredientResponse createIngredient(IngredientRequest request);
    IngredientResponse updateIngredient(Long id, IngredientRequest request);
    List<IngredientResponse> findAllIngredients();
    void deleteIngredient(Long id);

    // Operaciones de inventario
    IngredientResponse addStock(Long ingredientId, BigDecimal quantity, String reason);
    IngredientResponse reduceStock(Long ingredientId, BigDecimal quantity, String reason);
    IngredientResponse adjustStock(Long ingredientId, BigDecimal newQuantity, String reason);
    List<IngredientResponse> findLowStockIngredients();
    List<InventoryMovementResponse> getMovementHistory(Long ingredientId);

    boolean hasEnoughStock(Long id, BigDecimal requiredQuantity);
}