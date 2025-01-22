package com.palaspapas.back.dto.mapper;


import com.palaspapas.back.domain.RecipeItem;
import com.palaspapas.back.dto.request.RecipeItemRequest;
import com.palaspapas.back.dto.response.RecipeItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {IIngredientDtoMapper.class})
public interface IRecipeItemDtoMapper {

    @Mapping(source = "ingredientId", target = "ingredient.id")
    RecipeItem toDomain(RecipeItemRequest request);

    RecipeItemResponse toResponse(RecipeItem recipeItem);
}