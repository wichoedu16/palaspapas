package com.palaspapas.back.services;

import com.palaspapas.back.model.dto.request.sale.OrderRequest;
import com.palaspapas.back.model.dto.response.sale.OrderResponse;
import com.palaspapas.back.model.entity.sale.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest request);
    OrderResponse updateStatus(Long id, OrderStatus newStatus);
    OrderResponse findById(Long id);
    OrderResponse findByOrderNumber(String orderNumber);
    List<OrderResponse> findByStatus(OrderStatus status);
    List<OrderResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    void cancel(Long id);
}
