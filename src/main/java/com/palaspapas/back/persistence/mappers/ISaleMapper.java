package com.palaspapas.back.persistence.mappers;

import com.palaspapas.back.domain.Sale;
import com.palaspapas.back.persistence.entities.SaleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IProductMapper.class, IIngredientMapper.class})
public interface ISaleMapper {
    @Mapping(target = "details", source = "details")
    SaleEntity toEntity(Sale sale);

    @Mapping(target = "details", source = "details")
    Sale toDomain(SaleEntity entity);

    List<Sale> toDomainList(List<SaleEntity> entities);
}