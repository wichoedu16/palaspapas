package com.palaspapas.back.mapper;

import com.palaspapas.back.model.dto.request.sale.OrderRequest;
import com.palaspapas.back.model.dto.response.sale.OrderResponse;
import com.palaspapas.back.model.entity.sale.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderDetailMapper.class})
public interface OrderMapper {
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "total", ignore = true)
    Order toEntity(OrderRequest request);

    @Mapping(target = "details", source = "details")
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);
}