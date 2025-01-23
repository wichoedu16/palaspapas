package com.palaspapas.back.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponse {
    private Long id;
    private LocalDateTime saleDate;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal total;
    private String saleStatus;
    private String paymentType;
    private String notes;
    private Long createdBy;
    private Long approvedBy;
    private List<SaleDetailResponse> details;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}