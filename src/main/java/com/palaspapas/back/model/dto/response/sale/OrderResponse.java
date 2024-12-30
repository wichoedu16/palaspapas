package com.palaspapas.back.model.dto.response.sale;

import com.palaspapas.back.model.entity.sale.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;
    private String customerName;
    private String customerPhone;
    private List<OrderDetailResponse> details;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
