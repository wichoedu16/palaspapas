package com.palaspapas.back.persistence.mappers;

import com.palaspapas.back.domain.SaleDetail;
import com.palaspapas.back.persistence.entities.SaleDetailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
@Mapper(componentModel = "spring", uses = {IProductMapper.class, IIngredientMapper.class})
public interface ISaleDetailMapper {
    @Mapping(target = "sale", ignore = true)
    SaleDetailEntity toEntity(SaleDetail detail);

    SaleDetail toDomain(SaleDetailEntity entity);


    void updateEntity(@MappingTarget SaleDetailEntity entity, SaleDetail detail);
}
