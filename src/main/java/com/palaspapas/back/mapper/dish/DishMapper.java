package com.palaspapas.back.mapper.dish;

import com.palaspapas.back.model.dto.request.dish.DishRequest;
import com.palaspapas.back.model.dto.response.dish.DishResponse;
import com.palaspapas.back.model.entity.dish.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface DishMapper {
    @Mapping(target = "ingredients", ignore = true)
    @Mapping(target = "cost", ignore = true)
    @Mapping(target = "category", ignore = true)
    Dish toEntity(DishRequest request);

    @Mapping(target = "category", source = "category")
    DishResponse toResponse(Dish dish);
}