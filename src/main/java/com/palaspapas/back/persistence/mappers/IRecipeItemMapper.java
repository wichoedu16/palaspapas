package com.palaspapas.back.persistence.mappers;

import com.palaspapas.back.domain.RecipeItem;
import com.palaspapas.back.persistence.entities.RecipeItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {IIngredientMapper.class})
public interface IRecipeItemMapper {

    @Mapping(target = "product", ignore = true)
    RecipeItemEntity toEntity(RecipeItem recipeItem);

    RecipeItem toDomain(RecipeItemEntity entity);
}
