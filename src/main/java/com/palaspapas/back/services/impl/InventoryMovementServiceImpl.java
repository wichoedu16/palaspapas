package com.palaspapas.back.services.impl;

import com.palaspapas.back.exceptions.NotFoundException;
import com.palaspapas.back.exceptions.common.ValidationException;
import com.palaspapas.back.exceptions.inventory.InsufficientStockException;
import com.palaspapas.back.exceptions.inventory.InvalidMovementException;
import com.palaspapas.back.mapper.inventory.InventoryMovementMapper;
import com.palaspapas.back.model.dto.request.inventory.InventoryMovementRequest;
import com.palaspapas.back.model.dto.response.inventory.InventoryMovementResponse;
import com.palaspapas.back.model.entity.inventory.Ingredient;
import com.palaspapas.back.model.entity.inventory.InventoryMovement;
import com.palaspapas.back.repository.inventory.IngredientRepository;
import com.palaspapas.back.repository.inventory.InventoryMovementRepository;
import com.palaspapas.back.services.InventoryMovementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InventoryMovementServiceImpl implements InventoryMovementService {
    private final InventoryMovementRepository movementRepository;
    private final IngredientRepository ingredientRepository;
    private final InventoryMovementMapper movementMapper;

    @Override
    public InventoryMovementResponse createMovement(InventoryMovementRequest request) {
        log.debug("Creando nuevo movimiento de inventario para ingrediente ID: {}", request.getIngredientId());

        // Obtener y validar el ingrediente
        Ingredient ingredient = findIngredient(request.getIngredientId());

        // Validar el movimiento antes de procesarlo
        validateMovement(request, ingredient);

        // Crear el movimiento
        InventoryMovement movement = movementMapper.toEntity(request);
        movement.setIngredient(ingredient);

        // Actualizar el stock del ingrediente
        updateIngredientStock(ingredient, request);

        // Guardar el movimiento
        movement = movementRepository.save(movement);

        // Guardar el ingrediente actualizado
        ingredientRepository.save(ingredient);

        log.info("Movimiento de inventario creado con ID: {}", movement.getId());
        return movementMapper.toResponse(movement);
    }

    private void validateMovement(InventoryMovementRequest request, Ingredient ingredient) {
        // Validar que la cantidad sea positiva
        if (request.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("La cantidad debe ser mayor que cero");
        }

        // Para salidas, validar que haya suficiente stock
        if (ingredient.getCurrentStock().compareTo(request.getQuantity()) < 0) {
                throw new InsufficientStockException(
                        String.format( "Stock insuficiente para el ingrediente '%s'", ingredient.getName()
                        ),
                        ingredient.getCurrentStock(),
                        request.getQuantity());
        }

        // Validar el tipo de movimiento
        if (!isValidMovementType(request.getMovementType())) {
            throw new InvalidMovementException("Tipo de movimiento inválido: " + request.getMovementType());
        }
    }

    private void updateIngredientStock(Ingredient ingredient, InventoryMovementRequest request) {
        BigDecimal newStock;

        switch (request.getMovementType()) {
            case "I":
                newStock = ingredient.getCurrentStock().add(request.getQuantity());
                // Actualizar costo promedio si es una entrada
                updateAverageCost(ingredient, request.getQuantity(), request.getSellingPrice());
                break;
            case "V":
                newStock = ingredient.getCurrentStock().subtract(request.getQuantity());
                break;
            case "A":
                newStock = ingredient.getCurrentStock().add(request.getQuantity());
                break;
            default:
                throw new InvalidMovementException("Tipo de movimiento no soportado");
        }

        ingredient.setCurrentStock(newStock);

        // Verificar si se alcanzó el stock mínimo
        checkLowStock(ingredient);
    }

    private void updateAverageCost(Ingredient ingredient, BigDecimal quantity, BigDecimal newCost) {
        BigDecimal currentTotalValue = ingredient.getCurrentStock()
                .multiply(ingredient.getCurrentUnitCost());
        BigDecimal newTotalValue = quantity.multiply(newCost);
        BigDecimal newTotalStock = ingredient.getCurrentStock().add(quantity);

        BigDecimal newAverageCost = currentTotalValue.add(newTotalValue)
                .divide(newTotalStock, 2, RoundingMode.HALF_UP);

        ingredient.setCurrentUnitCost(newAverageCost);
    }

    private void checkLowStock(Ingredient ingredient) {
        if (ingredient.getCurrentStock().compareTo(ingredient.getMinimumStock()) <= 0) {
            log.warn("Stock bajo para ingrediente: {}. Stock actual: {}, Mínimo: {}",
                    ingredient.getName(),
                    ingredient.getCurrentStock(),
                    ingredient.getMinimumStock()
            );
            // Aquí podríamos agregar lógica para notificaciones de stock bajo
        }
    }

    private boolean isValidMovementType(String movementType) {
        return Arrays.asList("ENTRY", "EXIT", "ADJUSTMENT").contains(movementType);
    }

    private Ingredient findIngredient(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ingrediente no encontrado con ID: " + id));
    }

    @Override
    public List<InventoryMovementResponse> findByIngredient(Long ingredientId) {
        return null;
    }

    @Override
    public List<InventoryMovementResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return null;
    }

}