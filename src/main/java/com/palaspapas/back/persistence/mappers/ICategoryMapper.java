package com.palaspapas.back.persistence.mappers;

import com.palaspapas.back.domain.Category;
import com.palaspapas.back.persistence.entities.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {

    CategoryEntity toEntity(Category category);

    Category toDomain(CategoryEntity entity);

    void updateEntity(@MappingTarget CategoryEntity entity, Category category);
}