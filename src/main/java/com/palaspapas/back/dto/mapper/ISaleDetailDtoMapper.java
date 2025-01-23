package com.palaspapas.back.dto.mapper;

import com.palaspapas.back.domain.SaleDetail;
import com.palaspapas.back.dto.request.SaleDetailRequest;
import com.palaspapas.back.dto.response.SaleDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",  uses = {IProductDtoMapper.class, IIngredientDtoMapper.class})
public interface ISaleDetailDtoMapper {

    //@Mapping(source = "productId", target = "product.id")
    //@Mapping(source = "ingredientId", target = "ingredient.id")
    SaleDetail toDomain(SaleDetailRequest request);

    SaleDetailResponse toResponse(SaleDetail detail);
}