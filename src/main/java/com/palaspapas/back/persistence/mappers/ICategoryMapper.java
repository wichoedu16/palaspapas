package com.palaspapas.back.persistence.mappers;

import com.palaspapas.back.domain.Category;
import com.palaspapas.back.persistence.entities.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {
    CategoryEntity toEntity(Category category);
    Category toDomain(CategoryEntity entity);

}