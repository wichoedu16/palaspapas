package com.palaspapas.back.services.impl;

import com.palaspapas.back.exceptions.common.DuplicateResourceException;
import com.palaspapas.back.exceptions.common.ResourceNotFoundException;
import com.palaspapas.back.exceptions.inventory.InsufficientStockException;
import com.palaspapas.back.exceptions.inventory.InvalidMovementException;
import com.palaspapas.back.mapper.inventory.IngredientMapper;
import com.palaspapas.back.mapper.inventory.InventoryMovementMapper;
import com.palaspapas.back.model.dto.request.inventory.IngredientRequest;
import com.palaspapas.back.model.dto.response.inventory.IngredientResponse;
import com.palaspapas.back.model.dto.response.inventory.InventoryMovementResponse;
import com.palaspapas.back.model.entity.inventory.Ingredient;
import com.palaspapas.back.model.entity.inventory.InventoryMovement;
import com.palaspapas.back.repository.inventory.IngredientRepository;
import com.palaspapas.back.repository.inventory.InventoryMovementRepository;
import com.palaspapas.back.repository.provider.ProviderRepository;
import com.palaspapas.back.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para gestionar el inventario.
 * Proporciona la lógica de negocio para crear, actualizar, consultar y eliminar categorías de inventario.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {
    private final IngredientRepository ingredientRepository;
    private final InventoryMovementRepository movementRepository;
    private final ProviderRepository providerRepository;
    private final IngredientMapper ingredientMapper;
    private final InventoryMovementMapper movementMapper;

    @Override
    public IngredientResponse createIngredient(IngredientRequest request) {
        log.debug("Creando nuevo ingrediente: {}", request.getName());

        validateUniqueIngredientName(request.getName());
        if (request.getProviderId() != null) {
            validateProviderExists(request.getProviderId());
        }

        Ingredient ingredient = ingredientMapper.toEntity(request);
        ingredient = ingredientRepository.save(ingredient);

        log.info("Ingrediente creado con ID: {}", ingredient.getId());
        return ingredientMapper.toResponse(ingredient);
    }

    @Override
    public IngredientResponse updateIngredient(Long id, IngredientRequest request) {
        return null;
    }

    @Override
    public IngredientResponse addStock(Long ingredientId, BigDecimal quantity, String reason) {
        log.debug("Agregando stock para ingrediente ID: {}, cantidad: {}", ingredientId, quantity);

        validateQuantity(quantity);
        Ingredient ingredient = ingredientRepository.findById(ingredientId).get();

        // Crear movimiento
        InventoryMovement movement = createMovement(
                ingredient,
                "I",
                quantity,
                ingredient.getCurrentUnitCost(),
                reason
        );

        // Actualizar stock
        BigDecimal newStock = ingredient.getCurrentStock().add(quantity);
        ingredient.setCurrentStock(newStock);

        ingredient = ingredientRepository.save(ingredient);
        movementRepository.save(movement);

        log.info("Stock agregado exitosamente. Ingrediente ID: {}, Nuevo stock: {}", ingredientId, newStock);
        return ingredientMapper.toResponse(ingredient);
    }

    @Override
    public IngredientResponse reduceStock(Long ingredientId, BigDecimal quantity, String reason) {
        log.debug("Reduciendo stock para ingrediente ID: {}, cantidad: {}", ingredientId, quantity);

        validateQuantity(quantity);
        Ingredient ingredient = ingredientRepository.findById(ingredientId).get();
        validateSufficientStock(ingredient, quantity);

        // Crear movimiento
        InventoryMovement movement = createMovement(
                ingredient,
                "V",
                quantity,
                ingredient.getCurrentUnitCost(),
                reason
        );

        // Actualizar stock
        BigDecimal newStock = ingredient.getCurrentStock().subtract(quantity);
        ingredient.setCurrentStock(newStock);

        ingredient = ingredientRepository.save(ingredient);
        movementRepository.save(movement);

        // Verificar stock bajo
        checkLowStock(ingredient);

        log.info("Stock reducido exitosamente. Ingrediente ID: {}, Nuevo stock: {}", ingredientId, newStock);
        return ingredientMapper.toResponse(ingredient);
    }

    @Override
    public IngredientResponse adjustStock(Long ingredientId, BigDecimal newQuantity, String reason) {
        return null;
    }

    @Override
    public List<IngredientResponse> findLowStockIngredients() {
        return ingredientRepository.findLowStockIngredients().stream()
                .map(ingredientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryMovementResponse> getMovementHistory(Long ingredientId) {
        return null;
    }

    @Override
    public boolean hasEnoughStock(Long id, BigDecimal requiredQuantity) {
        return false;
    }

    // Métodos privados de ayuda
    private void validateQuantity(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidMovementException("La cantidad debe ser mayor que cero");
        }
    }

    private void validateSufficientStock(Ingredient ingredient, BigDecimal quantity) {
        if (ingredient.getCurrentStock().compareTo(quantity) < 0) {
            throw new InsufficientStockException(
                    String.format("Stock insuficiente para el ingrediente '%s'", ingredient.getName()),
                    ingredient.getCurrentStock(),
                    quantity
            );
        }
    }

    private void checkLowStock(Ingredient ingredient) {
        if (ingredient.getCurrentStock().compareTo(ingredient.getMinimumStock()) <= 0) {
            log.warn("Stock bajo detectado. Ingrediente: {}, Stock actual: {}, Stock mínimo: {}",
                    ingredient.getName(),
                    ingredient.getCurrentStock(),
                    ingredient.getMinimumStock()
            );
        }
    }

    private InventoryMovement createMovement(
            Ingredient ingredient,
            String type,
            BigDecimal quantity,
            BigDecimal unitCost,
            String reason) {
        return InventoryMovement.builder()
                .ingredient(ingredient)
                .type(type)
                .quantity(quantity)
                .unitCost(unitCost)
                .reason(reason)
                .build();
    }

    private void validateUniqueIngredientName(String name) {
        if (ingredientRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Ya existe un ingrediente con el nombre: " + name);
        }
    }

    private void validateProviderExists(Long providerId) {
        if (!providerRepository.existsById(providerId)) {
            throw new ResourceNotFoundException("Proveedor no encontrado con ID: " + providerId);
        }
    }

    @Override
    public List<IngredientResponse> findAllIngredients() {
        return null;
    }

    @Override
    public void deleteIngredient(Long id) {
        return;
    }
}