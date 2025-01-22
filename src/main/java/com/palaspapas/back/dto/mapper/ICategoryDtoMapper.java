package com.palaspapas.back.dto.mapper;

import com.palaspapas.back.domain.Category;
import com.palaspapas.back.dto.request.CategoryRequest;
import com.palaspapas.back.dto.response.CategoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICategoryDtoMapper {

    Category toDomain(CategoryRequest request);

    CategoryResponse toResponse(Category category);
}