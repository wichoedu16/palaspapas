package com.palaspapas.back.mapper.inventory;

import com.palaspapas.back.model.dto.request.inventory.InventoryMovementRequest;
import com.palaspapas.back.model.dto.response.inventory.InventoryMovementResponse;
import com.palaspapas.back.model.entity.inventory.InventoryMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface InventoryMovementMapper {
    @Mapping(target = "ingredient", ignore = true)
    InventoryMovement toEntity(InventoryMovementRequest request);

    @Mapping(target = "ingredientId", source = "ingredient.id")
    @Mapping(target = "ingredientName", source = "ingredient.name")
    InventoryMovementResponse toResponse(InventoryMovement movement);
}