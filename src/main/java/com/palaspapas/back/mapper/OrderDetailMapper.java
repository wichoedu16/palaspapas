package com.palaspapas.back.mapper;

import com.palaspapas.back.model.dto.request.sale.OrderDetailRequest;
import com.palaspapas.back.model.dto.response.sale.OrderDetailResponse;
import com.palaspapas.back.model.entity.sale.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "dish", ignore = true)
    OrderDetail toEntity(OrderDetailRequest request);

    @Mapping(target = "dishId", source = "dish.id")
    @Mapping(target = "dishName", source = "dish.name")
    OrderDetailResponse toResponse(OrderDetail detail);

    default String getMeasurementUnitSymbol(OrderDetail detail) {
        // Implementar lógica para obtener el símbolo de la unidad de medida
        return detail.getDish() != null ? "unidad" : null; // Ejemplo simplificado
    }
}
