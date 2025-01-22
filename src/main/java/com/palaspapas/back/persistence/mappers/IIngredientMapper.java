package com.palaspapas.back.persistence.mappers;

import com.palaspapas.back.domain.Ingredient;
import com.palaspapas.back.persistence.entities.IngredientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ICategoryMapper.class})
public interface IIngredientMapper {

    IngredientEntity toEntity(Ingredient ingredient);

    Ingredient toDomain(IngredientEntity entity);

    void updateEntity(@MappingTarget IngredientEntity entity, Ingredient ingredient);
}
