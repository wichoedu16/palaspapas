package com.palaspapas.back.dto.mapper;

import com.palaspapas.back.domain.Sale;
import com.palaspapas.back.dto.request.CreateSaleRequest;
import com.palaspapas.back.dto.response.SaleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IProductDtoMapper.class, IIngredientDtoMapper.class})
public interface ISaleDtoMapper {

    //@Mapping(target = "saleDate", expression = "java(LocalDateTime.now())")
    //@Mapping(target = "saleStatus", constant = "PENDING")
    //@Mapping(source = "paymentType", target = "paymentType")
    Sale toDomain(CreateSaleRequest request);

    SaleResponse toResponse(Sale sale);

    List<SaleResponse> toResponseList(List<Sale> sales);
}