package com.palaspapas.back.dto.mapper;

import com.palaspapas.back.domain.Product;
import com.palaspapas.back.dto.request.ProductRequest;
import com.palaspapas.back.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {ICategoryDtoMapper.class, IRecipeItemDtoMapper.class})
public interface IProductDtoMapper {

    @Mapping(source = "categoryId", target = "category.id")
    Product toDomain(ProductRequest request);

    ProductResponse toResponse(Product product);
}