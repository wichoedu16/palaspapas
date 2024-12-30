package com.palaspapas.back.mapper.inventory;

import com.palaspapas.back.mapper.provider.ProviderMapper;
import com.palaspapas.back.model.dto.request.inventory.IngredientRequest;
import com.palaspapas.back.model.dto.response.inventory.IngredientResponse;
import com.palaspapas.back.model.entity.inventory.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProviderMapper.class})
public interface IngredientMapper {
    @Mapping(target = "movements", ignore = true)
    @Mapping(target = "active", constant = "true")
    Ingredient toEntity(IngredientRequest request);

    @Mapping(target = "provider", source = "provider")
    IngredientResponse toResponse(Ingredient ingredient);
}