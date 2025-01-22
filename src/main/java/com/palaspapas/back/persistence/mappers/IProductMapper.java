package com.palaspapas.back.persistence.mappers;


import com.palaspapas.back.domain.Product;
import com.palaspapas.back.persistence.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
        uses = {ICategoryMapper.class, IRecipeItemMapper.class})
public interface IProductMapper {

    ProductEntity toEntity(Product product);

    Product toDomain(ProductEntity entity);

    void updateEntity(@MappingTarget ProductEntity entity, Product product);
}