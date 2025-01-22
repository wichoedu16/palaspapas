package com.palaspapas.back.dto.mapper;

import com.palaspapas.back.domain.Ingredient;
import com.palaspapas.back.dto.request.IngredientRequest;
import com.palaspapas.back.dto.response.IngredientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ICategoryDtoMapper.class})
public interface IIngredientDtoMapper {

    @Mapping(source = "categoryId", target = "category.id")
    Ingredient toDomain(IngredientRequest request);

    IngredientResponse toResponse(Ingredient ingredient);
}