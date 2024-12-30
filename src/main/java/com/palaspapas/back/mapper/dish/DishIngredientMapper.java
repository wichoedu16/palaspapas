package com.palaspapas.back.mapper.dish;

import com.palaspapas.back.model.dto.request.dish.DishIngredientRequest;
import com.palaspapas.back.model.dto.response.dish.DishIngredientResponse;
import com.palaspapas.back.model.entity.dish.DishIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface DishIngredientMapper {
    @Mapping(target = "dish", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    DishIngredient toEntity(DishIngredientRequest request);

    @Mapping(target = "ingredientId", source = "ingredient.id")
    @Mapping(target = "ingredientName", source = "ingredient.name")
    @Mapping(target = "measurementUnit", expression = "java(dishIngredient.getIngredient().getMeasurementUnit().getSymbol())")
    @Mapping(target = "totalCost", expression = "java(calculateTotalCost(dishIngredient))")
    DishIngredientResponse toResponse(DishIngredient dishIngredient);

    default BigDecimal calculateTotalCost(DishIngredient dishIngredient) {
        if (dishIngredient.getQuantity() != null && dishIngredient.getUnitCost() != null) {
            return dishIngredient.getQuantity().multiply(dishIngredient.getUnitCost());
        }
        return BigDecimal.ZERO;
    }
}