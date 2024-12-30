package com.palaspapas.back.mapper.dish;

import com.palaspapas.back.model.dto.request.dish.CategoryRequest;
import com.palaspapas.back.model.dto.response.dish.CategoryResponse;
import com.palaspapas.back.model.dto.response.dish.CategorySummaryResponse;
import com.palaspapas.back.model.entity.dish.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper para la conversión entre entidades CategoryProduct y sus DTO correspondientes.
 * Utiliza MapStruct para generar las implementaciones en tiempo de compilación.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    Category toEntity(CategoryRequest request);

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    CategoryResponse toResponse(Category category);

    CategorySummaryResponse toSummaryResponse(Category category);

    List<CategorySummaryResponse> toSummaryResponseList(List<Category> categories);
}
